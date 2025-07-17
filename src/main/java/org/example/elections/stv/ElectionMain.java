package org.example.elections.stv;
import java.util.Vector;

public class ElectionMain {
    public static void main(String[] args) {

        Vector<Candidate> candidates = new Vector<>();
        Vector<Vote> votes = new Vector<>();

        Candidate ALICE = new Candidate("Alice");
        Candidate BOB = new Candidate("Bob");
        Candidate CHARLIE = new Candidate("Charlie");
        Candidate DAVE = new Candidate("Dave");
        Candidate ERIN = new Candidate("Erin");
        Candidate FAYTHE = new Candidate("Faythe");
        candidates.add(ALICE);
        candidates.add(BOB);
        candidates.add(CHARLIE);
        candidates.add(DAVE);
        candidates.add(ERIN);
        candidates.add(FAYTHE);

		votes.add(new Vote(ALICE, BOB, CHARLIE, DAVE));
		votes.add(new Vote(BOB, ERIN, CHARLIE, ALICE, ERIN));
		votes.add(new Vote(BOB, FAYTHE, CHARLIE, ALICE, DAVE));
		votes.add(new Vote(FAYTHE, BOB, CHARLIE, ALICE));

        MeekAlgorithm m = new MeekAlgorithm(candidates, votes, 3);
        m.perform();
    }
}