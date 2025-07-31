package org.example.math.primes;

import java.util.Vector;

public class GermainPrimes {
    public static void main(String[] args) {
        int maxLength = 0;
        for(int i = 2; i < 10000000; i++) {
            Vector<Integer> gPrimes = getGermainPrimes(i);
            String s = "";
            for(int j = 0; j < gPrimes.size(); j++) {
                s += "->" + gPrimes.get(j);
            }
            if(gPrimes.size()>maxLength) {
                System.out.println(s.substring(2));
                maxLength = gPrimes.size();
            }
        }
    }

    private static Vector<Integer> getGermainPrimes(int p) {
        Vector<Integer> gPrimes = new Vector<>();
        while(isPrime(p)) {
            gPrimes.add(p);
            p = 2*p+1;
        }
        return gPrimes;
    }

    private static boolean isPrime(int p) {
        for(int i = 2; i < Math.sqrt(p+1); i++) {
            if(p % i == 0) {
                return false;
            }
        }
        return true;
    }
}
