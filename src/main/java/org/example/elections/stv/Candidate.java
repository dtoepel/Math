package org.example.elections.stv;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

public class Candidate implements Aligned {

    public Candidate(String name, Alignment alignment){
        this.name = name;
        this.random= Math.random();
        this.alignment = alignment;
    }

    public transient double weight = 1.0;
    public transient CandidateStatus status = CandidateStatus.HOPEFUL;
    public final String name;
    public double random;
    private final Alignment alignment;
    private Party party = null;

    public Alignment getAlignment() {
        return alignment;
    }
    public Party getParty() { return party; }

    public void setBestParty(Vector<Party> parties) {
        Collections.sort(parties, alignment.getRanker());
        this.party = parties.firstElement();
    }

    public static class VoteSorter implements Comparator<Candidate> {
        private Count count;

        public VoteSorter(Count count) {
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
