package com.techyourchance.unittestingfundamentals.exercise1;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class NegativeNumberValidatorTest {

    NegativeNumberValidator SUT;

    @Before
    public void setUp() throws Exception {
        SUT = new NegativeNumberValidator();
    }

    @Test
    public void sut_negative_returnTrue() {

        boolean result = SUT.isNegative(-3);
        assertThat(result, is(true));
    }

    @Test
    public void sut_positive_returnFalse() {
        boolean result = SUT.isNegative(4);
        assertThat(result, is(false));
    }


    @Test
    public void sut_zero_returnFalse() {
        boolean result = SUT.isNegative(0);
        assertThat(result, is(false));
    }
}