package org.example.elections.stv;

import java.awt.*;
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
    public final double random;
    private final Alignment alignment;
    private Party party = null;
    private Color color;

    public Alignment getAlignment() {
        return alignment;
    }
    public Party getParty() { return party; }

    public void setBestParty(Vector<Party> parties) {
        Collections.sort(parties, alignment.getRanker());
        this.party = parties.firstElement();
    }

    public String getNameAndParty() {
        return name + " (" + (party==null?"ind.":party.getName()) + ")";
    }

    public Color getColor() {
        if(color == null) return getParty().getColor();
        return color;
    }

    public void setColor(Color color) {this.color = color;}
    public void setParty(Party party) {this.party = party;}

    public static class VoteSorter implements Comparator<Candidate> {
        private final Count count;

        public VoteSorter(Count count) {
            this.count = count;
        }

        @Override
        public int compare(Candidate c1, Candidate c2) {
            if(count.voteCount().get(c1) > count.voteCount().get(c2)) return 1;
            if(count.voteCount().get(c1) < count.voteCount().get(c2)) return -1;
            return Double.compare(c1.random, c2.random);
        }
    }
}
