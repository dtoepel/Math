package org.example.elections.stv;

import java.util.Vector;

/**
 * An election can be held under different conditions.
 * A list of candidates is required in any case.
 * Lists of parties, voters and votes may be required.
 */
public class Election {

    Vector<Candidate> candidates = new Vector<>();
    Vector<Voter> voters = new Vector<>();
    Vector<Party> parties = new Vector<>();
    Vector<Vote> votes = new Vector<>();

    MeekAlgorithm m = null;

    public void addCandidate(Candidate c) {candidates.add(c);}
    public void addParty(Party p) {parties.add(p);}
    public void addVoter(Voter voter) {voters.add(voter);}

    /**
     * Assigns the most aligned party to each candidate which doesn't have a party set yet.
     * Only meaningful, if at least one candidate, and at least two parties exist.
     */
    public void assignPartyToCandidates() {
        for(Candidate c : candidates)
            if(c.getParty() == null)
                c.setBestParty(parties);
    }

    /**
     * Holds an election, given the number of available seats.
     * THe voters cast their votes automatically.
     *
     * @param numOfSeats the number of available seats
     */
    public void holdElection(int numOfSeats) {
        for(Voter voter : voters) {
            votes.add(voter.vote(new Vector<>(candidates)));
        }

        m = new MeekAlgorithm(candidates, votes, numOfSeats);
        m.setDebug(true);
    }

    public Vector<Candidate> getCandidates() {return candidates;}

    public VoteByFirstChoiceResult getVotesForCandidateByFirstChoice(Candidate candidate) {
        double[] _votes = new double[candidates.size()];
        Candidate[] _candidates = candidates.toArray(new Candidate[0]);
        for(int i = 0; i < _candidates.length; i++) {
            _votes[i] = 0.0;
            Candidate firstChoice = _candidates[i];
            for(Vote vote : votes) {
                // only take votes into account where first choices matches
                // as such, all votes are considered exactly once
                // namely when 1st choice matches
                if(firstChoice == vote.ranking.getFirst()) {
                    // start with the vote value,
                    // which is 1 be default
                    double voteValue = vote.amount;
                    for(int n = 0; n < vote.ranking.size() && voteValue > 0; n++) {
                        Candidate nthChoice = vote.ranking.get(n);
                        if(nthChoice == candidate) {
                            // then add this to the result
                            _votes[i] += voteValue * nthChoice.weight;
                        } else {
                            // otherwise the candidate has not been
                            // reached in the ranking.
                            // reduce vote by other candidate's share
                            voteValue *= (1-nthChoice.weight);
                        }
                    }
                }
            }
        }
        return new VoteByFirstChoiceResult(_candidates, _votes);
    }

    public double getFinalQuota() {
        return m.getQuota();
    }

    public void proceed() {
        m.performAsync();
    }

    public record VoteByFirstChoiceResult(Candidate[] candidates, double[] votes) {}
}
