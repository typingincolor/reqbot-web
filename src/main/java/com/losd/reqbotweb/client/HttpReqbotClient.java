package com.losd.reqbotweb.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.losd.reqbotweb.config.ReqbotClientConfiguration;
import com.losd.reqbotweb.exception.ReqbotWebException;
import com.losd.reqbotweb.model.ReqbotRequest;
import com.losd.reqbotweb.model.Response;
import org.apache.http.client.fluent.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

//import com.losd.reqbotweb.model.Request;

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
@Component
public class HttpReqbotClient implements ReqbotClient {
    Logger logger = LoggerFactory.getLogger(HttpReqbotClient.class);
    @Autowired
    ReqbotClientConfiguration config;

    @Override
    public List<String> getBuckets() {
        List<String> buckets;

        try {
            String result = Request.Get(config.getUrl() + "/buckets").execute().returnContent().asString();
            Gson gson = new GsonBuilder().serializeNulls().create();

            Type listType = new TypeToken<LinkedList<String>>() {
            }.getType();

            buckets = gson.fromJson(result, listType);
        } catch (Exception e) {
            logger.error("Error getting buckets", e);
            throw new ReqbotWebException(e);
        }
        return buckets;
    }

    @Override
    public List<ReqbotRequest> getByBucket(String bucket) {
        List<ReqbotRequest> requests = new LinkedList<>();

        try {
            String result = Request.Get(config.getUrl() + "/buckets/" + bucket).execute().returnContent().asString();
            Gson gson = new GsonBuilder().serializeNulls().create();

            Type listType = new TypeToken<LinkedList<ReqbotRequest>>() {
            }.getType();

            requests = gson.fromJson(result, listType);

        } catch (Exception e) {
            throw new RuntimeException("Error getting requests for bucket");
        } finally {
            return requests;
        }
    }

    @Override
    public List<String> getTags() {
        return null;
    }

    @Override
    public List<Response> getByTag(String tag) {
        return null;
    }

    @Override
    public Response get(String response) {
        return null;
    }

    @Override
    public void save(Response newResponse) {

    }
}
