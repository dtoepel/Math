package org.example.elections.stv;

import java.util.*;

public class Voter implements Aligned {
    private Alignment alignment;
    private double minAlignment = .5;

    public Voter(Alignment alignment) {
        this.alignment = alignment;
    }

    public Vote vote(List<Candidate> candidates) {
        Collections.sort(candidates, alignment.getRanker());
        Vector<Candidate> rank = new Vector<>();
        for (Candidate candidate : candidates) {
            double d = candidate.getAlignment().alignsWith(alignment);
            if (d >= minAlignment) rank.add(candidate);
        }
        return new Vote(rank);
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }
}
