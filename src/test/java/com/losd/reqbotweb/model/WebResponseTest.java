package com.losd.reqbotweb.model;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;

/**
 * The MIT License (MIT)
 * <p>
 * Copyright (c) 2015 Andrew Braithwaite
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
public class WebResponseTest {
    @Test
    public void it_produces_the_correct_json() {
        WebResponse test = new WebResponse();
        test.setHeaders("header1:value1\r\nheader2:value2");
        test.setTags("a,b,c,d");
        test.setBody("body");

        assertThat(test.toJson(), is(equalTo("{\"headers\":{\"header2\":\"value2\",\"header1\":\"value1\"},\"tags\":[\"a\",\"b\",\"c\",\"d\"],\"body\":\"body\"}")));
    }

    @Test
    public void it_handles_headers_with_dashes() {
        WebResponse test = new WebResponse();
        test.setHeaders("header-1:value-1");

        assertThat(test.toJson(), is(equalTo("{\"headers\":{\"header-1\":\"value-1\"},\"tags\":[],\"body\":null}")));
    }

    @Test
    public void it_handles_headers_with_dots() {
        WebResponse test = new WebResponse();
        test.setHeaders("header.1:value.1");

        assertThat(test.toJson(), is(equalTo("{\"headers\":{\"header.1\":\"value.1\"},\"tags\":[],\"body\":null}")));
    }

    @Test
    public void it_handles_headers_with_spaces() {
        WebResponse test = new WebResponse();
        test.setHeaders("header-1   :  value-1");

        assertThat(test.toJson(), is(equalTo("{\"headers\":{\"header-1\":\"value-1\"},\"tags\":[],\"body\":null}")));
    }

    @Test
    public void it_handles_headers_with_slashes() {
        WebResponse test = new WebResponse();
        test.setHeaders("header-1:value/1");

        assertThat(test.toJson(), is(equalTo("{\"headers\":{\"header-1\":\"value/1\"},\"tags\":[],\"body\":null}")));
    }

    @Test
    public void it_handles_tags_with_spaces() {
        WebResponse test = new WebResponse();
        test.setTags("a, b, c,    d");

        assertThat(test.toJson(), is(equalTo("{\"headers\":{},\"tags\":[\"a\",\"b\",\"c\",\"d\"],\"body\":null}")));
    }
}
