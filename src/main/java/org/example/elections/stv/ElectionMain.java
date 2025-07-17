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
        votes.add(new Vote(CHARLIE, FAYTHE, BOB, ALICE));

        MeekAlgorithm m = new MeekAlgorithm(candidates, votes, 3);
        System.out.println("\nDefault algorithm for 3 seats:");
        m.setDebug(true);
        m.perform();

        System.out.println("\nAgain 3 seats, but with a seat guaranteed for Alice:");
        m.reset();
        ALICE.status=CandidateStatus.ELECTED;
        m.perform();

        /*Vector<Vote> votes2 = new Vector<>();
        for(int i = 0; i < 3; i++)  votes2.add(new Vote(ALICE));
        for(int i = 0; i < 3; i++)  votes2.add(new Vote(BOB));
        for(int i = 0; i < 2; i++)  votes2.add(new Vote(BOB,ALICE));
        MeekAlgorithm m2 = new MeekAlgorithm(candidates, votes2, 1);
        m2.setDebug(true);
        m2.setMinApproval(2./3);
        m2.perform();*/


    }
}