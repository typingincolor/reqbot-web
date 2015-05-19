package com.losd.reqbotweb.client;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.losd.reqbotweb.config.ReqbotClientConfiguration;
import com.losd.reqbotweb.exception.ReqbotWebException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.contains;
import static com.github.tomakehurst.wiremock.client.WireMock.*;

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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ReqbotClientConfiguration.class, HttpReqbotClient.class})
public class ReqbotClientTest {
    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8080);

    @Autowired
    HttpReqbotClient client;

    @Test
    public void it_gets_a_list_of_buckets() throws Exception {
        stubFor(get(urlEqualTo("/buckets"))
                .willReturn(
                        aResponse().withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody("[\"aa\",\"bb\",\"cc\",\"dd\"]")));

        List<String> result =  client.getBuckets();
        assertThat(result, hasSize(4));
        assertThat(result, contains("aa", "bb", "cc", "dd"));

        verify(getRequestedFor(urlMatching("/buckets")));
    }

    @Test(expected = ReqbotWebException.class)
    public void it_handles_a_bad_request_when_getting_a_list_of_buckets() throws Exception {
        stubFor(get(urlEqualTo("/buckets"))
                .willReturn(
                        aResponse().withStatus(500).withBody("Server Error")));

        client.getBuckets();
    }
}
