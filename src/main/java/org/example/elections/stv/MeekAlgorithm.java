package org.example.elections.stv;

import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

public class MeekAlgorithm {

    private Vector<Candidate> candidates;
    private Vector<Vote> votes;
    private int seats;

    public MeekAlgorithm(Vector<Candidate> candidates, Vector<Vote> votes, int seats)  {
        this.candidates = candidates;
        this.votes = votes;
        this.seats = seats;
    }

    public void perform() {
        init();
        int remainingCandidates = countRemainingCandidates();

        while(remainingCandidates > seats) {
            System.out.println("\n" + seats + " Seats available, " + remainingCandidates + " Candidates Remaining.");

            findWeights();
            {Count count = countVotes();
                printVoteCount(count.voteCount, count.excess, count.quota);}

//			electCandidates();

            int electedCandidates = electCandidates();

            if(electedCandidates == 0) excludeLast();

            remainingCandidates = countRemainingCandidates();
        }

//		findWeights();

        for(Candidate c : candidates) if (c.status == CandidateStatus.HOPEFUL) {
            c.status = CandidateStatus.ELECTED;
            System.out.println("\n\t\u001B[32m" + c.name + " elected !!!\u001B[0m");
        }
        {Count count = countVotes();
            printVoteCount(count.voteCount, count.excess, count.quota);}
    }

    private void excludeLast() {
        Count count = countVotes();
        Vector<Candidate> hopefulCandidates = new Vector<>();
        for(Candidate c : candidates) if (c.status == CandidateStatus.HOPEFUL) hopefulCandidates.add(c);

        Collections.sort(hopefulCandidates, new Candidate.Sorter(count));
        hopefulCandidates.get(0).status = CandidateStatus.EXCLUDED;
        hopefulCandidates.get(0).weight = 0.0;

        System.out.println("\n\t\u001B[31m" + hopefulCandidates.get(0).name + " excluded !!!\u001B[0m");

    }

    private int electCandidates() {
        Count count = countVotes();
        int elected = 0;
        for(Candidate c : candidates) {
            if(c.status == CandidateStatus.HOPEFUL) {
                if(count.voteCount.get(c) >= count.quota) {
                    c.status = CandidateStatus.ELECTED;
                    elected++;
                    System.out.println("\n\t\u001B[32m" + c.name + " elected !!!\u001B[0m");
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
                if(count.voteCount.get(c)/count.quota < 0.9999) ok = false;
                if(count.voteCount.get(c)/count.quota > 1.0001) ok = false;
            }
        }
        if(!ok) {
            for(Candidate c : candidates) {
                if(c.status == CandidateStatus.ELECTED) {
                    c.weight = c.weight * count.quota / count.voteCount.get(c);
                }
            }
            findWeights();
        }
    }

    private void printVoteCount(HashMap<Candidate, Double> voteCount, double excess, double quota) {
        double a = 0.;
        for(Vote v : votes) a+=v.amount;
        System.out.println("\nTotal Votes = "+a);
        for(Candidate c : candidates) {
            String terminalColor = c.status == CandidateStatus.ELECTED?"\u001B[32m":
                                   c.status == CandidateStatus.HOPEFUL?"\u001B[33m":"\u001B[31m";
            System.out.println(terminalColor + c.name + ": " + voteCount.get(c) + " Votes. (weight=" + c.weight + ") " + c.status.toString() + "\u001B[0m");
        }
        System.out.println("Quota = "+quota);
        System.out.println("Excess = "+excess);
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

    private void init() {
        for(Candidate c : candidates) {
            c.status = CandidateStatus.HOPEFUL;
            c.weight = 1.;
        }
    }
}
