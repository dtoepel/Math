package org.example.elections.stv;

import java.util.Vector;

public class Election {

    Vector<Candidate> candidates = new Vector<>();
    Vector<Voter> voters = new Vector<>();
    Vector<Party> parties = new Vector<>();
    Vector<Vote> votes = new Vector<>();

    public void addCandidate(Candidate c) {candidates.add(c);}
    public void addParty(Party p) {parties.add(p);}

    public void assignPartyToCandidates() {

    }
}
