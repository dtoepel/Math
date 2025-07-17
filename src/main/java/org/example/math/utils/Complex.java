package org.example.math.utils;

public record Complex(double r, double i) {
    public static final Complex ONE = new Complex(1, 0);
    public static final Complex ZERO = new Complex(0, 0);

    public static Complex fromPolar(double abs, double theta) {
        return new Complex(Math.cos(theta) * abs, Math.sin(theta) * abs);
    }

    public double dist(Complex that) {
        return Math.sqrt((this.r - that.r) * (this.r - that.r) + (this.i - that.i) * (this.i - that.i));
    }

    public Complex scale(double s) {
        return new Complex(s * r, s * i);
    }

    public Complex mul(Complex that) {
        return new Complex(this.r * that.r - this.i * that.i, this.r * that.i + this.i * that.r);
    }

    public Complex add(Complex that) {
        return new Complex(this.r + that.r, this.i + that.i);
    }

    public Complex sub(Complex that) {
        return this.add(that.scale(-1));
    }

    public Complex div(Complex that) {
        double f = that.r * that.r + that.i * that.i;
//		if(f == 0) throw new RuntimeException("DIV/0");
        return new Complex(
                (this.r * that.r + this.i * that.i) / f,
                (this.i * that.r - this.r * that.i) / f);
    }

    public double abs() {
        return dist(ZERO);
    }

    public double theta() {
        return Math.atan2(this.i, this.r);
    }

    @Override
    public String toString() {
        if (i == 0) return "" + r;
        if (r == 0) return i + "i";
        return r + (i < 0 ? ("-" + (-i)) : ("+" + i)) + "i";
    }

    public static void main(String[] args) throws Exception {
        for (Complex c : new Complex[]{new Complex(0, 1), new Complex(1, 1), new Complex(-1, 0)}) {
            System.out.println(c + " --> abs:" + c.abs() + " theta: " + c.theta());
        }
    }

    public Complex firstRoot(int nth) {
        return fromPolar(Math.pow(this.abs(), 1. / nth), (this.theta() + 2 * Math.PI) / nth);
    }

    public Complex complement() {
        return new Complex(this.r, -this.i);
    }
}
