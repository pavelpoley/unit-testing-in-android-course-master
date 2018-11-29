package com.techyourchance.unittestingfundamentals.exercise2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;

public class StringDuplicatorTest {
    
    StringDuplicator sut;
    
    @Before
    public void setUp() throws Exception {
        sut = new StringDuplicator();
    }

    @Test
    public void sut_emptyString_returnEmptyString() {
        String result = sut.duplicate("");

        Assert.assertThat(result,is(""+""));
    }

    @Test
    public void sut_string_returnString() {
        String result = sut.duplicate("pavel");

        Assert.assertThat(result,is("pavel"+"pavel"));
    }


}