package org.example.elections.stv;

import java.util.HashMap;

public class Count {
    public HashMap<Candidate, Double> voteCount;
    public double total;
    public double excess;
    public double quota;

    public Count(HashMap<Candidate, Double> voteCount, double total, double excess, double quota) {
        this.voteCount = voteCount;
        this.total = total;
        this.excess = excess;
        this.quota = quota;
    }
}
