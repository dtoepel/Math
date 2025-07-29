package org.example.elections.stv.examples;

import org.example.elections.stv.*;
import org.example.elections.stv.Dimension;

import java.awt.*;

public class Example1 {
    public static void main(String[] args) {
        Election election = new Election();

        Candidate c0 = new Candidate("Gloria Stevens", null);
        Candidate c1 = new Candidate("Denise Wilson", null);
        Candidate c2 = new Candidate("Ethan Turner", null);
        Candidate c3 = new Candidate("Michael Murphy", null);
        Candidate c4 = new Candidate("Sandra Thompson", null);
        Candidate c5 = new Candidate("Jessica Murphy", null);
        Candidate c6 = new Candidate("Carol White", null);
        Candidate c7 = new Candidate("Jason Jones", null);
        Candidate c8 = new Candidate("Douglas Shaw", null);
        Candidate c9 = new Candidate("Olivia Lee", null);

        election.addCandidate(c0);
        election.addCandidate(c1);
        election.addCandidate(c2);
        election.addCandidate(c3);
        election.addCandidate(c4);
        election.addCandidate(c5);
        election.addCandidate(c6);
        election.addCandidate(c7);
        election.addCandidate(c8);
        election.addCandidate(c9);

        Alignment a0 = new Alignment();
        a0.setAlignment(Dimension.AUTHORITARIAN_DEMOCRATIC, .9);
        a0.setAlignment(Dimension.INTERNATIONAL_NATIONAL, -.5);
        a0.setAlignment(Dimension.LIBERAL_CONSERVATIVE, -.3);

        Party greens = new Party(a0, "Green Party", "GREEN", Color.GREEN);
        Party grayParty = new Party(new Alignment(), "Gray Party", "GRAY", Color.GRAY);

        election.addParty(greens);
        election.addParty(grayParty);

        election.assignPartyToCandidates();
    }
}
