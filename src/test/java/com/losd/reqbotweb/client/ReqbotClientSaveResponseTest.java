package com.losd.reqbotweb.client;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.losd.reqbotweb.model.Response;
import com.losd.reqbotweb.model.WebResponse;
import org.hamcrest.Matchers;
import org.junit.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


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
public class ReqbotClientSaveResponseTest extends ReqbotClientTest {
    @Test
    public void it_saves_a_response() throws Exception {
        WebResponse webResponse = new WebResponse();
        webResponse.setHeaders("header1:value1\r\nheader2:value2");
        webResponse.setTags("a,b,c,d");
        webResponse.setBody("body");

        stubFor(post(urlEqualTo("/responses"))
                .withRequestBody(WireMock.equalTo(webResponse.toJson()))
                .withHeader("Content-Type", WireMock.equalTo("application/json"))
                        .willReturn(
                                aResponse().withStatus(200)
                                        .withHeader("Content-Type", "application/json")
                                        .withBodyFile("saved_response.json")));

        Response response = client.save(webResponse);
        assertThat(response.getHeaders(), hasEntry("header1", "value1"));
        assertThat(response.getHeaders(), hasEntry("header2", "value2"));
        assertThat(response.getBody(), is(Matchers.equalTo("body")));
        assertThat(response.getTags(), containsInAnyOrder("a", "b", "c", "d"));

        verify(postRequestedFor(urlMatching("/responses")));
    }

    @Test(expected = HttpReqbotClientException.class)
    public void it_handles_a_failure() throws Exception {
        WebResponse webResponse = new WebResponse();
        webResponse.setHeaders("header1:value1\r\nheader2:value2");
        webResponse.setTags("a,b,c,d");
        webResponse.setBody("body");

        stubFor(post(urlEqualTo("/responses"))
                .withRequestBody(WireMock.equalTo(webResponse.toJson()))
                .willReturn(
                        aResponse().withStatus(500)));

        client.save(webResponse);
    }
}
