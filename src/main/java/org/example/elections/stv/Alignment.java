package org.example.elections.stv;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Alignment {
    private final double SPEED = .3;
    private final Ranker ranker = new Ranker();

    private final HashMap<Dimension, Double> values = new HashMap<>();

    public static Alignment random() {
        Alignment a = new Alignment();
        a.setAlignment(Dimension.FOSSILE_GREEN, Math.random()*2-1);
        a.setAlignment(Dimension.SOCIAL_INDIVIDUAL, Math.random()*2-1);
        a.setAlignment(Dimension.RELIGIOUS_SECULAR, Math.random()*2-1);
        a.setAlignment(Dimension.UNIONIST_SEPARATIST, Math.random()*2-1);
        a.setAlignment(Dimension.LIBERAL_CONSERVATIVE, Math.random()*2-1);
        a.setAlignment(Dimension.INTERNATIONAL_NATIONAL, Math.random()*2-1);
        a.setAlignment(Dimension.AUTHORITARIAN_DEMOCRATIC, Math.random()*2-1);
        a.setAlignment(Dimension.UNPOPULAR_POPULAR, 0);
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
            alignsWith *= (1.- diff * SPEED); // between 1-SPEED and 1
            //alignsWith *= (1.-Math.pow(diff, SPEED));
        }
        return alignsWith;
    }

    public Alignment mix(Alignment that, double otherShare) {
        Set<Dimension> dimensions = new HashSet<>(this.values.keySet());
        Set<Dimension> dimensions2 = new HashSet<>(that.values.keySet());
        //System.err.println(dimensions);
        //System.err.println(dimensions2);
        dimensions.addAll(dimensions2);
        Alignment result = new Alignment();
        for(Dimension dimension : dimensions) {
            result.values.put(dimension,
                    this.values.getOrDefault(dimension,0.) * (1-otherShare) +
                    that.values.getOrDefault(dimension,0.) * (otherShare));
        }
        return result;
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

    public Alignment() {
        setAlignment(Dimension.UNPOPULAR_POPULAR, 0);
    }

    private Alignment(Alignment original) {
        for(Dimension dim : original.values.keySet()) {
            values.put(dim, original.values.get(dim));
        }
    }

    public Alignment withAbs(Dimension dim, double val) {
        Alignment a = new Alignment(this);
        a.setAlignment(dim, val);
        return a;
    }

    public Alignment withRel(Dimension dim, double val) {
        Alignment a = new Alignment(this);
        double oldVal = values.get(dim);
        double div = Math.max(1, 1 + val * oldVal);
        a.setAlignment(dim, (val+oldVal)/div);
        return a;
    }
}
