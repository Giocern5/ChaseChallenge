package com.example.chasechallenge;

import org.junit.Test;
import static org.junit.Assert.*;

import com.example.chasechallenge.utils.Utils;

public class UtilsTest {

    @Test
    public void kelvinToFahrenheit_withValidKelvin_returnsCorrectValue() {
        double kelvin = 300.0; // Example Kelvin temperature
        String result = Utils.kelvinToFahrenheit(kelvin);
        assertEquals("80", result); // Expected Fahrenheit value for 300K is 80°F
    }
}