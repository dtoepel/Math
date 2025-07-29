package org.example.elections.stv.examples;

import org.example.elections.stv.*;
import org.example.elections.stv.Dimension;

import java.awt.*;

public class Example1 {
    public static void main(String[] args) {
        Election election = new Election();

        Candidate c0 = new Candidate("Gloria Stevens", Alignment.random());
        Candidate c1 = new Candidate("Denise Wilson", Alignment.random());
        Candidate c2 = new Candidate("Ethan Turner", Alignment.random());
        Candidate c3 = new Candidate("Michael Murphy", Alignment.random());
        Candidate c4 = new Candidate("Sandra Thompson", Alignment.random());
        Candidate c5 = new Candidate("Jessica Murphy", Alignment.random());
        Candidate c6 = new Candidate("Carol White", Alignment.random());
        Candidate c7 = new Candidate("Jason Jones", Alignment.random());
        Candidate c8 = new Candidate("Douglas Shaw", Alignment.random());
        Candidate c9 = new Candidate("Olivia Lee", Alignment.random());

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

        for(int i = 0; i < 100; i++) {
            Alignment a = Alignment.random();
            Voter voter = new Voter(a);
            election.addVoter(voter);
        }

        election.holdElection(4);


    }
}
