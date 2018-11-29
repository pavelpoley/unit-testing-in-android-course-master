package com.techyourchance.unittestingfundamentals.exercise3;

import com.techyourchance.unittestingfundamentals.example3.Interval;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class IntervalsAdjacencyDetectorTest {

    IntervalsAdjacencyDetector sut;

    @Before
    public void setUp() throws Exception {
        sut = new IntervalsAdjacencyDetector();
    }

    @Test
    public void isAdjacent_i1_before_i2_returnFalse() {

        Interval interval1 = new Interval(1, 3);
        Interval interval2 = new Interval(8, 12);

        boolean result = sut.isAdjacent(interval1, interval2);

        Assert.assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_i1_adjacent_i2_returnTrue() {

        Interval interval1 = new Interval(1, 3);
        Interval interval2 = new Interval(3, 12);

        boolean result = sut.isAdjacent(interval1, interval2);

        Assert.assertThat(result, is(true));
    }

    @Test
    public void isAdjacent_i1_overlap_i2_returnfalse() {

        Interval interval1 = new Interval(1, 3);
        Interval interval2 = new Interval(2, 12);

        boolean result = sut.isAdjacent(interval1, interval2);

        Assert.assertThat(result, is(false));
    }


    @Test
    public void isAdjacent_i1_contain_i2_returnfalse() {

        Interval interval1 = new Interval(1, 10);
        Interval interval2 = new Interval(2, 7);

        boolean result = sut.isAdjacent(interval1, interval2);

        Assert.assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_i2_contain_i1_returnfalse() {

        Interval interval1 = new Interval(2, 7);
        Interval interval2 = new Interval(1, 10);

        boolean result = sut.isAdjacent(interval1, interval2);

        Assert.assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_i1_same_i2_returnfalse() {

        Interval interval1 = new Interval(1, 7);
        Interval interval2 = new Interval(1, 7);

        boolean result = sut.isAdjacent(interval1, interval2);

        Assert.assertThat(result, is(false));
    }

    @Test
    public void isAdjacent_i1_after_i2_returnfalse() {

        Interval interval1 = new Interval(7, 10);
        Interval interval2 = new Interval(1, 5);

        boolean result = sut.isAdjacent(interval1, interval2);

        Assert.assertThat(result, is(false));
    }
}