package com.losd.reqbotweb.controller;

import com.losd.reqbotweb.client.ReqbotClient;
import com.losd.reqbotweb.model.Response;
import com.losd.reqbotweb.model.WebResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;

import static org.cthul.matchers.CthulMatchers.matchesPattern;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
public class WebControllerCreateResponsesTest {
    private MockMvc mockMvc;

    @Mock
    private ReqbotClient client;

    @InjectMocks
    private WebController webController;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("blah");
        mockMvc = MockMvcBuilders.standaloneSetup(webController).setViewResolvers(viewResolver).build();
    }

    @Test
    public void it_creates_a_response() throws Exception {
        Response result = new Response.Builder().addHeader("header1", "value1")
                .tags(Arrays.asList("a", "b", "c"))
                .body("body").build();

        when(client.save(any(WebResponse.class))).thenReturn(result);

        mockMvc.perform(post("/web/response/create")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("tags", "a,b,c")
                .param("headers", "header1:value1")
                .param("body", "body"))
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attribute("response", is(equalTo(result))))
                .andExpect(view().name(matchesPattern("redirect:/web/responses/" + "[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}")));

        ArgumentCaptor<WebResponse> argumentCaptor = ArgumentCaptor.forClass(WebResponse.class);

        verify(client, times(1)).save(argumentCaptor.capture());

        assertThat(argumentCaptor.getValue().getHeaders(), is(equalTo("header1:value1")));
        assertThat(argumentCaptor.getValue().getTags(), is(equalTo("a,b,c")));
        assertThat(argumentCaptor.getValue().getBody(), is(equalTo("body")));
    }
}
