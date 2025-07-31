package org.example.elections.stv;

import java.util.HashMap;
import java.util.Vector;

public class MeekAlgorithm {

    private final Vector<Candidate> candidates;
    private final Vector<Vote> votes;
    private final int seats;

    private boolean debug = false;

    /**
     * Inits the Algorithm with a list of candidates and a list of votes cast.
     * This does not start the distribution, so that other parameters can be set before.
     * Initializes all candidates as if reset had been invoked.
     * The distribution is performed using perform().
     * @param candidates The candidates standing in the election
     * @param votes The votes cast
     * @param seats The number of seats to be awarded
     */
    public MeekAlgorithm(Vector<Candidate> candidates, Vector<Vote> votes, int seats)  {
        this.candidates = candidates;
        this.votes = votes;
        this.seats = seats;
        reset();
    }

    /**
     * Distributes the available seats to the standing candidates.
     * The status of all candidates should be HOPEFUL,
     * which is the case right after using the constructor.
     * Other configurations may yield unexpected results.
     * Candidates which have a different status will keep that status.
     *
     * This method performs the entire algorithm in one go.
     * For a pause after each step, use performAsync until it returns true
     */
    public void perform() {
        while(!performAsync()) {}
    }

    /**
     * Distributes the available seats as perform() does, but step by step.
     *
     * This method only performs one step each time it is invoked.
     * If it has not terminated, it retains its state and returns false.
     *
     * If invoked again the next step is performed.
     * If the algorithm has terminated, true is returned and
     * any further invocation will not have any effect.
     */
    public boolean performAsync() {
        // next iteration:
        // what to do next?
        // first count remaining (!ELIMINATED) Candidates
        int remainingCandidates = countRemainingCandidates();

        // if candidates have been reduced to available seats, then finalize
        // i.e. set all remaining HOPEFUL to ELECTED
        if(remainingCandidates <= seats) {
            boolean changed = false;
            for(Candidate c : candidates) {
                if (c.status == CandidateStatus.HOPEFUL) {
                    c.status = CandidateStatus.ELECTED;
                    if (debug) System.out.println("\n\t\u001B[32m" + c.getNameAndParty() + " elected !!!\u001B[0m");
                    // notify that the algorithm has terminated.
                    // if tried again, the algorithm will go through here again unchanged
                    changed = true;
                }
                if(changed) {
                    Count count = countVotes();
                    if(debug) printVoteCount(count.voteCount(), count.excess(), count.quota());
                }
            }
            return true;
        }

        // otherwise there are too many candidates left.
        if(debug) System.out.println("\n" + seats + " Seats available, " + remainingCandidates + " Candidates Remaining.");

        Count count = countVotes();
        if(debug) printVoteCount(count.voteCount(), count.excess(), count.quota());

        // with the new weights (from last iteration), some candidates may be over the quota and are ELECTED
        // this function returns the number of newly elected candidates
        int electedCandidates = electCandidates();

        // if any candidate was newly elected
        // the algorithm stops, waiting for user input to proceed
        // otherwise, one candidate is excluded, equally pausing the algorithm.
        if(electedCandidates == 0) excludeLast();

        // find the new weights.
        // this will reduce the vote share kept by elected candidates,
        // and set the weight for excluded candidates to zero
        if(seats >= countRemainingCandidates()) {
            findWeights();
            return true;
        } else {
            findWeights();
        }
        return false;
    }

    private void excludeLast() {
        Count count = countVotes();
        Vector<Candidate> hopefulCandidates = new Vector<>();
        for(Candidate c : candidates) if (c.status == CandidateStatus.HOPEFUL) hopefulCandidates.add(c);

        hopefulCandidates.sort(new Candidate.VoteSorter(count));
        hopefulCandidates.getFirst().status = CandidateStatus.EXCLUDED;
        hopefulCandidates.getFirst().weight = 0.0;

        if(debug) System.out.println("\n\t\u001B[31m" + hopefulCandidates.getFirst().getNameAndParty() + " excluded !!!\u001B[0m");
    }

    private int electCandidates() {
        Count count = countVotes();
        int newlyElected = 0;
        for(Candidate c : candidates) {
            if(c.status == CandidateStatus.HOPEFUL) {
                if(count.voteCount().get(c) >= count.quota()) {
                    c.status = CandidateStatus.ELECTED;
                    newlyElected++;
                    if(debug) System.out.println("\n\t\u001B[32m" + c.getNameAndParty() + " elected !!!\u001B[0m");
                }
            }
        }
        return newlyElected;
    }

    private void findWeights() {
        Count count = countVotes();
        boolean ok = true;
        for(Candidate c : candidates) {
            if(c.status == CandidateStatus.ELECTED) {
                if(count.voteCount().get(c)/count.quota() < 0.9999) ok = false;
                if(count.voteCount().get(c)/count.quota() > 1.0001) ok = false;
            }
        }
        if(!ok) {
            for(Candidate c : candidates) {
                if(c.status == CandidateStatus.ELECTED) {
                    c.weight = c.weight * count.quota() / count.voteCount().get(c);
                }
            }
            findWeights();
        }
    }

    /**
     * prints a vote count to the console
     */
    public void printVoteCount() {
        Count count = countVotes();
        printVoteCount(count.voteCount(), count.excess(), count.quota());
    }

    private void printVoteCount(HashMap<Candidate, Double> voteCount, double excess, double quota) {
        double a = 0.;
        for(Vote v : votes) a+=v.amount;
        System.out.println("\nTotal Votes = "+a);
        for(Candidate c : candidates) {
            String terminalColor = c.status == CandidateStatus.ELECTED?"\u001B[32m":
                                   c.status == CandidateStatus.HOPEFUL?"\u001B[33m":"\u001B[31m";
            System.out.println(terminalColor + c.getNameAndParty() + ": " + voteCount.get(c) + " Votes. (weight=" + c.weight + ") " + c.status.toString() + "\u001B[0m");
        }
        System.out.println("Quota = "+quota);
        System.out.println("Excess = "+excess);
    }

    private Count countVotes() {
        double total = 0.;
        double excess;

        HashMap<Candidate, Double> voteCount = new HashMap<>();
        // initialize all candidates with zero votes
        for(Candidate c : candidates) {
            voteCount.put(c, 0.0);
        }
        // then process ballot by ballot
        for(Vote v : votes) {
            double vote = v.amount;
            total+=vote;
            for(Candidate c : v.ranking) {
                // for each rank, add the remaining vote * weight onto the candidate's count
                // and deduct that from the remaining vote
                voteCount.put(c, voteCount.get(c) + vote * c.weight);
                vote -= vote * c.weight;
            }
        }

        // to calculate the excess, count the number of votes and deduct non-exhausted, i.e. counted votes
        excess = total;
        for(Candidate c : candidates) excess-=voteCount.get(c);

        return new Count(voteCount, total, excess, (total-excess)/(seats+.999));
    }

    private int countRemainingCandidates() {
        int i = 0;
        for(Candidate c : candidates) {
            if(c.status != CandidateStatus.EXCLUDED) i++;
        }
        return i;
    }

    /**
     * Switches on messages on System.out explaining the status of the algorithm
     * @param debug if true prints messages
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Resets the algorithm, setting the status of all candidates to HOPEFUL.
     */
    public void reset() {
        for(Candidate c : candidates) {
            c.status = CandidateStatus.HOPEFUL;
            c.weight = 1.;
        }
    }

    public double getQuota() {
        return countVotes().quota();
    }
}
