package com.losd.reqbotweb.controller;

import com.losd.reqbotweb.client.ReqbotClient;
import com.losd.reqbotweb.model.Response;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class WebControllerTagViewTest {
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
    public void it_populates_the_tag_list_correctly() throws Exception {
        List<Response> responseList = new ArrayList<>();
        Map<String, String> headers = new HashMap<>();
        headers.put("header1", "value1");

        responseList.add(new Response.Builder()
                .headers(headers)
                .body("body")
                .build());

        when(client.getTags()).thenReturn(Arrays.asList("tag1", "tag2", "tag3"));
        when(client.getByTag("tag1")).thenReturn(responseList);

        mockMvc.perform(get("/tags/tag1"))
                .andExpect(status().isOk())
                .andExpect(view().name(is("tag-view")))
                .andExpect(model().attribute("mode", is(equalTo("response"))))
                .andExpect(model().attribute("tag", is("tag1")))
                .andExpect(model().attribute("tags", containsInAnyOrder("tag1", "tag2", "tag3")))
                .andExpect(model().attribute("responses", hasSize(1)))
                .andExpect(model().attribute("responses", is(responseList)));
    }
}
