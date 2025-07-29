package org.example.elections.stv;

import java.util.Comparator;

public class Candidate {

    public Candidate(String name, Alignment alignment) {
        this.name = name;
        this.random= Math.random();
    }

    public transient double weight = 1.0;
    public transient CandidateStatus status = CandidateStatus.HOPEFUL;
    public final String name;
    public double random;
    private Alignment alignment;

    public Alignment getAlignment() {
        return alignment;
    }

    public static class Sorter implements Comparator<Candidate> {
        private Count count;

        public Sorter(Count count) {
            this.count = count;
        }

        @Override
        public int compare(Candidate c1, Candidate c2) {
            if(count.voteCount().get(c1) > count.voteCount().get(c2)) return 1;
            if(count.voteCount().get(c1) < count.voteCount().get(c2)) return -1;
            if(c1.random > c2.random) return 1;
            if(c1.random < c2.random) return -1;
            return 0;
        }

    }

}
