package com.losd.reqbotweb.client;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.losd.reqbotweb.config.ReqbotClientConfiguration;
import com.losd.reqbotweb.model.ReqbotRequest;
import com.losd.reqbotweb.model.Response;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.LinkedList;
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
@Component
public class HttpReqbotClient implements ReqbotClient {
    Gson gson = new GsonBuilder().serializeNulls().create();

    Logger logger = LoggerFactory.getLogger(HttpReqbotClient.class);

    @Autowired
    ReqbotClientConfiguration config;

    @Override
    public List<String> getBuckets() {
        try {
            HttpResponse<String> result = Unirest.get(config.getUrl() + "/buckets").asString();

            switch(result.getStatus()) {
                case HttpStatus.SC_OK:
                    List<String> buckets = gson.fromJson(result.getBody(), getTypeLinkedListString());
                    return ImmutableList.copyOf(buckets);
                case HttpStatus.SC_NOT_FOUND:
                    return ImmutableList.copyOf(Collections.emptyList());
                default:
                    logger.error("Received HTTP status code of {} from reqbot api", result.getStatus());
                    throw new HttpReqbotClientException(result.getStatus());
            }
        }
        catch (UnirestException e) {
            logger.error("Something has gone wrong with Unirest", e);
            throw new HttpReqbotClientException(e);
        }
    }


    @Override
    public List<ReqbotRequest> getByBucket(String bucket) {
        try {
            HttpResponse<String> result = Unirest.get(config.getUrl() + "/buckets/" + bucket).asString();

            switch(result.getStatus()) {
                case HttpStatus.SC_OK:
                    List<ReqbotRequest> requests = gson.fromJson(result.getBody(), getTypeLinkedListReqbotRequest());
                    return ImmutableList.copyOf(requests);
                case HttpStatus.SC_NOT_FOUND:
                    return ImmutableList.copyOf(Collections.emptyList());
                default:
                    logger.error("Received HTTP status code of {} from reqbot api", result.getStatus());
                    throw new HttpReqbotClientException(result.getStatus());
            }
        }
        catch (UnirestException e) {
            logger.error("Something has gone wrong with Unirest", e);
            throw new HttpReqbotClientException(e);
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

    private Type getTypeLinkedListString() {
        return new TypeToken<LinkedList<String>>() {}.getType();
    }

    private Type getTypeLinkedListReqbotRequest() {
        return new TypeToken<LinkedList<ReqbotRequest>>() {}.getType();
    }

}
