package com.losd.reqbotweb.client;

import com.losd.reqbotweb.model.Response;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsMapContaining.hasEntry;
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
public class ReqbotClientGetResponseTest extends ReqbotClientTest {
    @Test
    public void it_gets_a_response() throws Exception {
        stubFor(get(urlEqualTo("/responses/bbf9c3de-1547-4423-8274-ffc79d07aaaa"))
                .willReturn(
                        aResponse().withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBodyFile("get_response.json")));

        Response result = client.getResponse("bbf9c3de-1547-4423-8274-ffc79d07aaaa");
        assertThat(result.getUuid().toString(), is(equalTo("bbf9c3de-1547-4423-8274-ffc79d07aaaa")));
        assertThat(result.getHeaders(), hasEntry("test_header2", "test_header_value2"));
        assertThat(result.getTags(), containsInAnyOrder("a", "b", "c", "aa", "ab"));
        assertThat(result.getBody(), is(equalTo("response_body")));

        verify(getRequestedFor(urlMatching("/responses/bbf9c3de-1547-4423-8274-ffc79d07aaaa")));
    }

    @Test(expected = HttpReqbotClientException.class)
    public void it_handles_a_bad_request_when_getting_a_response() throws Exception {
        stubFor(get(urlEqualTo("/responses/bbf9c3de-1547-4423-8274-ffc79d07aaaa"))
                .willReturn(
                        aResponse().withStatus(500).withBody("Server Error")));

        client.getResponse("bbf9c3de-1547-4423-8274-ffc79d07aaaa");
    }

    @Test(expected = ResponseNotFoundException.class)
    public void it_handles_not_finding_a_response() throws Exception {
        stubFor(get(urlEqualTo("/responses/bbf9c3de-1547-4423-8274-ffc79d07aaaa"))
                .willReturn(
                        aResponse().withStatus(404).withBody("Not Found")));

        client.getResponse("bbf9c3de-1547-4423-8274-ffc79d07aaaa");
    }
}
