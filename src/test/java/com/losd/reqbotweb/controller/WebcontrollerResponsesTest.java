package com.losd.reqbotweb.controller;

import com.losd.reqbotweb.client.ReqbotClient;
import com.losd.reqbotweb.model.Response;
import com.losd.reqbotweb.model.WebResponse;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.isA;
import static org.mockito.Mockito.*;
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
public class WebcontrollerResponsesTest {
    private MockMvc mockMvc;

    @Mock
    private ReqbotClient responseRepo;

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
    public void it_redirects_to_the_first_tag_page_if_there_are_tags() throws Exception {
        List<String> tagList = new LinkedList<>(Arrays.asList("tag1","tag2","tag3"));

        when(responseRepo.getTags()).thenReturn(tagList);

        mockMvc.perform(get("/responses")).andExpect(status().is3xxRedirection())
                .andExpect(view().name(is("redirect:/tags/tag1")));

        verify(responseRepo, times(1)).getTags();
    }

    @Test
    public void it_renders_the_reponse_page_if_there_are_no_tags() throws Exception {
        when(responseRepo.getTags()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/responses")).andExpect(status().isOk())
                .andExpect(view().name(is("index")))
                .andExpect(model().attribute("mode", is(equalTo("response"))));
    }

    @Test
    public void it_renders_the_correct_page_for_a_single_response() throws Exception {
        Response response = new Response.Builder().addHeader("header1", "value1").body("body").build();

        when(responseRepo.getResponse("1234")).thenReturn(response);

        mockMvc.perform(get("/responses/1234")).andExpect(status().isOk())
            .andExpect(view().name(is("response")))
            .andExpect(model().attribute("response", is(equalTo(response))));

        verify(responseRepo, times(1)).getResponse("1234");
    }

    @Test
    public void it_renders_the_create_response_page() throws Exception {
        mockMvc.perform(get("/responses/create")).andExpect(status().isOk())
                .andExpect(view().name(is("create-response")))
                .andExpect(model().attribute("webResponse", isA(WebResponse.class)));
    }


}
