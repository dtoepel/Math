package org.example.elections.stv;

import java.util.HashMap;

public record Count(
    HashMap<Candidate, Double> voteCount,
    double total,
    double excess,
    double quota) {}
