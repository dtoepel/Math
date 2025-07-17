package org.example.math.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexTest {
    @Test
    public void someComplexNumberCalculations() {
        // GIVEN
        int nth = 5;
        Complex c = Complex.ONE;
        // WHEN
        Complex nthRoot = c.firstRoot(nth);
        Complex result = Complex.ONE;
        for(int i = 0; i < nth; i++) {
            result = result.mul(nthRoot);
            //System.out.println(result);
        }
        // THEN
        assertEquals(c.abs(), result.abs(), 1.0e-10, "Absolute value wrong");
        assertEquals(c.theta(), result.theta(), 0x1.0p-20, "Wrong angle");
        assertEquals(c.r(), result.r(), 0x1.0p-20, "Real part wrong");
        assertEquals(c.i(), result.i(), 0x1.0p-20, "Imaginary part wrong");
        //assertEquals(c, result);
    }

    /*@Test
    public void divByZeroError() {
        assertThrows(ArithmeticException.class, () -> Complex.EINS.div(Complex.NULL));
    }*/

    @Test
    public void someMoreCalculations() {
        Complex a = new Complex(3,4);
        //Complex b = a.complement();
        Complex b = new Complex(3,-4);

        assertEquals(a.theta(), -b.theta(), 1.0e-10, "Angles must be inverse");
        assertEquals(0, a.mul(b).i(), 1.0e-10, "Imaginary part must be zero");

    }
}
