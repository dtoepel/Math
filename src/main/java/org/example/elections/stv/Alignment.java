package org.example.elections.stv;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Alignment {
    private final double SPEED = .9;
    private final Ranker ranker = new Ranker();

    private final HashMap<Dimension, Double> values = new HashMap<>();

    public static Alignment random() {
        Alignment a = new Alignment();
        a.setAlignment(Dimension.LEFT_RIGHT, Math.random()*2-1);
        a.setAlignment(Dimension.LIBERAL_CONSERVATIVE, Math.random()*2-1);
        a.setAlignment(Dimension.FOSSILE_GREEN, Math.random()*2-1);
        return a;
    }

    public void setAlignment(Dimension dim, double val) {
        if(val < -1||val > 1) throw new RuntimeException("Illegal value: " + val);
        values.put(dim, val);
    }

    public Ranker getRanker() {
        return ranker;
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

    public class Ranker implements Comparator<Aligned> {
        private Ranker() {}

        @Override
        public int compare(Aligned c1, Aligned c2) {
            Double c1A = c1.getAlignment().alignsWith(Alignment.this);
            Double c2A = c2.getAlignment().alignsWith(Alignment.this);
            return -c1A.compareTo(c2A);
        }
    }
}
