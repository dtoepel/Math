package org.example.math.newton;

import org.example.math.utils.Complex;

import java.util.HashMap;
import java.util.HashSet;

public class Polynom {
    public static Polynom EINS = null;

    private HashMap<Integer, Complex> koeffizienten = new HashMap<>();

    static{
        EINS = new Polynom();
        EINS.koeffizienten.put(0, Complex.ONE);
    }

    public Polynom(Complex nullstelle) {
        koeffizienten.put(1, Complex.ONE);
        koeffizienten.put(0, nullstelle.scale(-1));
    }

    private Polynom() {}

    public Polynom mul(Polynom that) {
        Polynom produkt = new Polynom();
        for(Integer iThis : this.koeffizienten.keySet()) {
            for(Integer iThat : that.koeffizienten.keySet()) {
                Integer iProduct = iThis + iThat;
                Complex val = Complex.ZERO;
                if(produkt.koeffizienten.containsKey(iProduct)) {
                    val = produkt.koeffizienten.get(iProduct);
                }
                produkt.koeffizienten.put(iProduct, val.add(this.koeffizienten.get(iThis).mul(that.koeffizienten.get(iThat))));
            }
        }
        return produkt;
    }

    @Override
    public String toString() {
        HashSet<Integer> usedInts = new HashSet<>();
        int i = 0;
        String s = "";
        while(!usedInts.containsAll(koeffizienten.keySet())) {
            if(i == 0)
                s = koeffizienten.get(0).toString();
            else
                s = "(" + koeffizienten.get(i) + ") * x^" + i + " + "+ s;
            usedInts.add(i);
            i++;
        }
        return s;
    }

    public Polynom ableiten() {
        Polynom ableitung = new Polynom();
        for(Integer i : this.koeffizienten.keySet()) {
            if(i != 0) {
                ableitung.koeffizienten.put(i-1, this.koeffizienten.get(i).scale(i));
            }
        }
        return ableitung;
    }

    public Complex at(Complex c) {
        Complex summe = Complex.ZERO;
        for(Integer i : this.koeffizienten.keySet()) {
            Complex x = Complex.ONE;
            for(int j = 0; j < i; j++) {
                x = x.mul(c);
            }
            x = x.mul(this.koeffizienten.get(i));
            summe = summe.add(x);
        }
        return summe;
    }
}
