package com.losd.reqbotweb.model;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.*;
import java.util.regex.MatchResult;

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
public class WebResponse {
    private String headers;
    private String tags;
    private String body;

    public String getHeaders() {
        return headers;
    }

    public void setHeaders(String headers) {
        this.headers = headers;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toJson() {
        Map<String, String> headers = getHeadersFromFormData(getHeaders());
        List<String> tags = getTagsFromFormData(getTags());

        Gson gson = new GsonBuilder().serializeNulls().create();

        JsonObject json = new JsonObject();
        json.add("headers", gson.toJsonTree(headers));
        json.add("tags", gson.toJsonTree(tags));
        json.add("body", gson.toJsonTree(getBody()));

        return gson.toJson(json);
    }

    private Map<String, String> getHeadersFromFormData(String formHeaders) {
        if (Strings.isNullOrEmpty(formHeaders)) {
            return Collections.emptyMap();
        }

        Map<String, String> headers = new HashMap<>();
        Scanner headerScanner = new Scanner(formHeaders);

        while (headerScanner.hasNextLine()) {
            Scanner lineScanner = new Scanner(headerScanner.nextLine());
            lineScanner.findInLine("([a-z0-9A-Z-\\./]+)\\s*:\\s*([a-z0-9A-Z-\\./]+)");
            MatchResult res = lineScanner.match();
            headers.put(res.group(1), res.group(2));
        }

        return headers;
    }

    private List<String> getTagsFromFormData(String formTags) {
        if (Strings.isNullOrEmpty(formTags)) {
            return Collections.emptyList();
        }

        List<String> tags = new LinkedList<>();
        Scanner tagScanner = new Scanner(formTags).useDelimiter("\\s*,\\s*");
        tagScanner.forEachRemaining((tag) -> tags.add(tag));

        return tags;
    }
}
