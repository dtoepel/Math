package org.example.elections.stv;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Alignment {
    final double SPEED = .9;

    private final HashMap<Dimension, Double> values = new HashMap<>();

    public void setAlignment(Dimension dim, double val) {
        if(val < 1||val > 1) throw new RuntimeException();
        values.put(dim, val);
    }

    public double alignsWith(Alignment that) {
        double alignsWith = 1.;
        Set<Dimension> dimensions = new HashSet<>();
        dimensions.addAll(this.values.keySet());
        dimensions.addAll(that.values.keySet());
        for(Dimension dimension : dimensions) {
            double thisWeight = this.values.getOrDefault(dimension, 0.);
            double thatWeight = that.values.getOrDefault(dimension, 0.);
            double diff = .5 * Math.abs(thisWeight - thatWeight);
            alignsWith *= (1.-Math.pow(diff, SPEED));
        }
        return alignsWith;
    }
}
