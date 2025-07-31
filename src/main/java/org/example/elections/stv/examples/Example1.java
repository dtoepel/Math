package org.example.elections.stv.examples;

import org.example.elections.stv.*;
import org.example.elections.stv.Dimension;
import org.example.elections.stv.graphics.STVResultPanel;

import java.awt.*;

public class Example1 {
    public static void main(String[] args) {
        Election election = new Election();

        Candidate c0 = new Candidate("Jessica Simpson",
                Party.THE_HALLOW_PARTY.getAlignment());
        Candidate c1 = new Candidate("Philine Wilson",
                Party.THE_HALLOW_PARTY.getAlignment()
                        .mix(Party.CENTER_PARTY.getAlignment(),.45)
                        .withRel(Dimension.UNPOPULAR_POPULAR, .7));
        Candidate c2 = new Candidate("Michael Murphy",
                Party.CONSERVATIVE_PARTY.getAlignment());
        Candidate c3 = new Candidate("Douglas Shaw",
                Party.CENTER_PARTY.getAlignment()
                        .mix(Party.CONSERVATIVE_PARTY.getAlignment(),.4));
        Candidate c4 = new Candidate("Olivia Lee",
                Party.CENTER_PARTY.getAlignment().withRel(Dimension.UNPOPULAR_POPULAR, .5));
        Candidate c5 = new Candidate("Kathryn Young",
                Party.CENTER_PARTY.getAlignment()
                        .mix(Party.LIBERAL_PARTY.getAlignment(),.4));
        Candidate c6 = new Candidate("Gloria Stevens",
                Party.LIBERAL_PARTY.getAlignment());
        Candidate c7 = new Candidate("Sandra Thompson",
                Party.GREEN_PARTY.getAlignment()
                        .mix(Party.LIBERAL_PARTY.getAlignment(),.4));
        Candidate c8 = new Candidate("Jason Jones",
                Party.GREEN_PARTY.getAlignment());
        Candidate c9 = new Candidate("Ethan Turner",
                Party.COMMUNIST_PARTY.getAlignment());
        Candidate c10 = new Candidate("Carol White",
                Party.GREY_PARTY.getAlignment()
                .withRel(Dimension.UNPOPULAR_POPULAR, .99));

        c0.setParty(Party.THE_HALLOW_PARTY);
        c1.setParty(Party.THE_HALLOW_PARTY);
        c2.setParty(Party.CONSERVATIVE_PARTY);
        c3.setParty(Party.CENTER_PARTY);
        c4.setParty(Party.CENTER_PARTY);
        c5.setParty(Party.CENTER_PARTY);
        c6.setParty(Party.LIBERAL_PARTY);
        c7.setParty(Party.GREEN_PARTY);
        c8.setParty(Party.GREEN_PARTY);
        c9.setParty(Party.COMMUNIST_PARTY);
        c10.setParty(Party.GREY_PARTY);

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
        election.addCandidate(c10);

        election.addParty(Party.GREY_PARTY);
        election.addParty(Party.THE_HALLOW_PARTY);
        election.addParty(Party.CONSERVATIVE_PARTY);
        election.addParty(Party.CENTER_PARTY);
        election.addParty(Party.LIBERAL_PARTY);
        election.addParty(Party.GREEN_PARTY);
        election.addParty(Party.COMMUNIST_PARTY);

        election.assignPartyToCandidates();

        c0.setColor(c1.getParty().getColor().brighter());
        c4.setColor(c3.getParty().getColor().brighter());
        c5.setColor(c4.getColor().brighter());
        c8.setColor(c7.getParty().getColor().brighter());

        for(int i = 0; i < 100; i++) {
            Alignment a = Party.THE_HALLOW_PARTY.getAlignment()
                    .mix(Party.CONSERVATIVE_PARTY.getAlignment(), i / 99.)
                    .withAbs(Dimension.UNPOPULAR_POPULAR, 1.);;
            Voter voterA = new Voter(a);
            election.addVoter(voterA);

            Alignment b = Party.CENTER_PARTY.getAlignment()
                    .mix(Party.CONSERVATIVE_PARTY.getAlignment(), i / 99.)
                    .withAbs(Dimension.UNPOPULAR_POPULAR, 1.);;
            Voter voterB = new Voter(b);
            election.addVoter(voterB);

            Alignment c = Party.CENTER_PARTY.getAlignment()
                    .mix(Party.LIBERAL_PARTY.getAlignment(), i / 99.)
                    .withAbs(Dimension.UNPOPULAR_POPULAR, 1.);;
            Voter voterC = new Voter(c);
            election.addVoter(voterC);

            Alignment d = Party.GREEN_PARTY.getAlignment()
                    .mix(Party.LIBERAL_PARTY.getAlignment(), i / 99.)
                    .withAbs(Dimension.UNPOPULAR_POPULAR, 1.);;
            Voter voterD = new Voter(d);
            election.addVoter(voterD);

            Alignment e = Party.GREEN_PARTY.getAlignment()
                    .mix(Party.COMMUNIST_PARTY.getAlignment(), i / 99.)
                    .withAbs(Dimension.UNPOPULAR_POPULAR, 1.);;
            Voter voterE = new Voter(e);
            election.addVoter(voterE);
        }
        for(int i = 0; i < 20; i++) {
            Alignment a = Alignment.random().withAbs(Dimension.UNPOPULAR_POPULAR, 1.);
            Voter voter = new Voter(a);
            election.addVoter(voter);
        }

        election.holdElection(1);

        STVResultPanel resultPanel = new STVResultPanel(election);
        resultPanel.showInFrame("2025 Election");

    }
}
