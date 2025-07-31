package org.example.elections.stv;

import java.util.Arrays;
import java.util.Vector;

public class Vote {

    public Vote(Candidate... candidates) {
        ranking = new Vector<>();
        ranking.addAll(Arrays.asList(candidates));
    }

    public Vote(Vector<Candidate> candidates) {
        ranking = new Vector<>(candidates);
    }

    public Vote(double amount, Candidate... candidates) {
        ranking = new Vector<>();
        ranking.addAll(Arrays.asList(candidates));
        this.amount = amount;
    }

    public double amount = 1.;
    public Vector<Candidate> ranking;

}