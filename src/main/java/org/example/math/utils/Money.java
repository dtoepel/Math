package org.example.math.utils;

import java.text.DecimalFormat;

public class Money {
    final double amount;
    final Currency currency;
    final DecimalFormat format = new DecimalFormat("0.00");

    public static void main(String[] args) throws Currency.IllegalCurrencyException {
        Currency.initWait();
        Money m = new Money(1, Currency.getCurrency("DEM"));
        System.out.println("  " + m + " = ");
        System.out.println(m.in(Currency.getCurrency("ATS")));
        System.out.println(m.in(Currency.getCurrency("ITL")));
        System.out.println(m.in(Currency.getCurrency("SDR")));

        Money n = new Money(1, Currency.getCurrency("GIP"));
        System.out.println("  " + n + " = ");
        System.out.println(n.in(Currency.getCurrency("JEP")));
        System.out.println(n.in(Currency.getCurrency("DEM")));
        System.out.println(n.in(Currency.getCurrency("SDR")));
        System.out.println(n.in(Currency.getCurrency("XPF")));
    }

    public Money(double amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public Money in(Currency currency) {
        return new Money(amount / this.currency.getPriceInEUR() * currency.getPriceInEUR(), currency);
    }



    @Override
    public String toString() {
        return format.format(amount) + " " + currency.getName();
    }
}
