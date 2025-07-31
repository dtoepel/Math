package org.example.elections.stv;

import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Voter implements Aligned {
    private final Alignment alignment;
    private final double minAlignment = .5;

    public Voter(Alignment alignment) {
        this.alignment = alignment;
    }

    public Vote vote(List<Candidate> candidates) {
        Collections.sort(candidates, alignment.getRanker());
        Vector<Candidate> rank = new Vector<>();
        for (Candidate candidate : candidates) {
            double d = candidate.getAlignment().alignsWith(alignment);
            if (d >= minAlignment || candidate==candidates.getFirst()) rank.add(candidate);
        }
        return new Vote(rank);
    }

    @Override
    public Alignment getAlignment() {
        return alignment;
    }
}
