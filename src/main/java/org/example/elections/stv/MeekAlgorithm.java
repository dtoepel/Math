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
     */
    public void perform() {
        int remainingCandidates = countRemainingCandidates();

        while(remainingCandidates > seats) {
            if(debug) System.out.println("\n" + seats + " Seats available, " + remainingCandidates + " Candidates Remaining.");

            findWeights();
            {Count count = countVotes();
                printVoteCount(count.voteCount(), count.excess(), count.quota());}

            int electedCandidates = electCandidates();

            if(electedCandidates == 0) excludeLast();

            remainingCandidates = countRemainingCandidates();
        }
        for(Candidate c : candidates) if (
                c.status == CandidateStatus.HOPEFUL) {
            c.status = CandidateStatus.ELECTED;
            if(debug) System.out.println("\n\t\u001B[32m" + c.name + " elected !!!\u001B[0m");
        }
        {Count count = countVotes();
            printVoteCount(count.voteCount(), count.excess(), count.quota());}
    }

    private void excludeLast() {
        Count count = countVotes();
        Vector<Candidate> hopefulCandidates = new Vector<>();
        for(Candidate c : candidates) if (c.status == CandidateStatus.HOPEFUL) hopefulCandidates.add(c);

        hopefulCandidates.sort(new Candidate.Sorter(count));
        hopefulCandidates.getFirst().status = CandidateStatus.EXCLUDED;
        hopefulCandidates.getFirst().weight = 0.0;

        if(debug) System.out.println("\n\t\u001B[31m" + hopefulCandidates.getFirst().name + " excluded !!!\u001B[0m");

    }

    private int electCandidates() {
        Count count = countVotes();
        int elected = 0;
        for(Candidate c : candidates) {
            if(c.status == CandidateStatus.HOPEFUL) {
                if(count.voteCount().get(c) >= count.quota()) {
                    c.status = CandidateStatus.ELECTED;
                    elected++;
                    if(debug) System.out.println("\n\t\u001B[32m" + c.name + " elected !!!\u001B[0m");
                }
            }
        }
        return elected;
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

    private void printVoteCount(HashMap<Candidate, Double> voteCount, double excess, double quota) {
        double a = 0.;
        for(Vote v : votes) a+=v.amount;
        if(debug) System.out.println("\nTotal Votes = "+a);
        for(Candidate c : candidates) {
            String terminalColor = c.status == CandidateStatus.ELECTED?"\u001B[32m":
                                   c.status == CandidateStatus.HOPEFUL?"\u001B[33m":"\u001B[31m";
            if(debug) System.out.println(terminalColor + c.name + ": " + voteCount.get(c) + " Votes. (weight=" + c.weight + ") " + c.status.toString() + "\u001B[0m");
        }
        if(debug) System.out.println("Quota = "+quota);
        if(debug) System.out.println("Excess = "+excess);
    }

    private Count countVotes() {
        double total = 0.;
        double excess;

        HashMap<Candidate, Double> voteCount = new HashMap<>();
        for(Candidate c : candidates) {
            voteCount.put(c, 0.0);
        }
        for(Vote v : votes) {
            double vote = v.amount;
            total+=vote;
            for(Candidate c : v.ranking) {
                voteCount.put(c, voteCount.get(c) + vote * c.weight);
                vote -= vote * c.weight;
            }
        }
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


}
