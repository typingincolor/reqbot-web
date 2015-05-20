package com.losd.reqbotweb.controller;

import com.losd.reqbotweb.client.ReqbotClient;
import com.losd.reqbotweb.client.ResponseNotFoundException;
import com.losd.reqbotweb.model.Response;
import com.losd.reqbotweb.model.WebResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

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
@Controller
public class WebController {
    @Autowired
    private ReqbotClient client = null;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(Model model) {
        List<String> buckets = client.getBuckets();

        if (buckets.size() > 0) {
            return "redirect:/buckets/" + buckets.get(0);
        }

        model.addAttribute("mode", "request");

        return "index";
    }

    @RequestMapping(value = "/buckets/{bucket}", method = RequestMethod.GET)
    public String viewBucket(@PathVariable String bucket, Model model) {
        model.addAttribute("mode", "request");
        model.addAttribute("bucket", bucket);
        model.addAttribute("buckets", client.getBuckets());
        model.addAttribute("requests", client.getByBucket(bucket));
        return "bucket-view";
    }

    @RequestMapping(value = "/tags/{tag}", method = RequestMethod.GET)
    public String viewTag(@PathVariable String tag, Model model) {
        model.addAttribute("mode", "response");
        model.addAttribute("tag", tag);
        model.addAttribute("tags", client.getTags());
        model.addAttribute("responses", client.getByTag(tag));

        return "tag-view";
    }

    @RequestMapping(value = "/responses", method = RequestMethod.GET)
    public String responses(Model model) {
        List<String> tags = client.getTags();

        if (tags.size() > 0) {
            return "redirect:/tags/" + tags.get(0);
        }

        model.addAttribute("mode", "response");

        return "index";
    }

    @RequestMapping(value = "/responses/{response}", method = RequestMethod.GET)
    public String response(@PathVariable String response, Model model) {
        try {
            Response result = client.getResponse(response);
            model.addAttribute("response", result);
            return "response";
        } catch (ResponseNotFoundException e) {
            return "error";
        }
    }

    @RequestMapping(value = "/responses/create", method = RequestMethod.GET)
    public String renderCreateReponse(Model model) {
        model.addAttribute("webResponse", new WebResponse());
        return "create-response";
    }

    @RequestMapping(value = "/responses", method = RequestMethod.POST)
    public String handleCreateReponseForm(@ModelAttribute WebResponse webResponse,Model model) {
        Response result = client.save(webResponse);

        model.addAttribute("response", result);
        return "redirect:/responses/" + result.getUuid();
    }
}
