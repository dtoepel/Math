package org.example.elections.stv;

import java.util.*;

public class Voter {
    private Alignment alignment;
    private double minAlignment;

    public Vote vote(List<Candidate> candidates) {
        Collections.sort(candidates, new CandidateRanker());
        Vector<Candidate> rank = new Vector<>();
        for (Candidate candidate : candidates) {
            double d = candidate.getAlignment().alignsWith(alignment);
            if (d >= minAlignment) rank.add(candidate);
        }
        return new Vote(rank);
    }

    private class CandidateRanker implements Comparator<Candidate> {
        @Override
        public int compare(Candidate c1, Candidate c2) {
            Double c1A = c1.getAlignment().alignsWith(alignment);
            Double c2A = c2.getAlignment().alignsWith(alignment);
            return -c1A.compareTo(c2A);
        }
    }
}
