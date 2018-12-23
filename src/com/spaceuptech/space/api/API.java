package com.spaceuptech.space.api;

import com.google.gson.Gson;
import com.spaceuptech.space.api.mongo.Mongo;
import com.spaceuptech.space.api.sql.SQL;
import com.spaceuptech.space.api.utils.Config;
import com.spaceuptech.space.api.utils.Utils;
import org.asynchttpclient.AsyncHttpClient;

import java.util.HashMap;

import static org.asynchttpclient.Dsl.asyncHttpClient;

public class API {
    private Config config;

    public API(String projectId, String url) {
        if (!url.endsWith("/")) url += "/";
        this.config = new Config(projectId, url, asyncHttpClient());
    }

    public void setToken(String token) {
        this.config.token = token;
    }

    public void setProjectId(String projectId) {
        this.config.projectId = projectId;
    }

    public Mongo Mongo() {
        return new Mongo(this.config);
    }

    public SQL MySQL() {
        return new SQL("mysql", this.config);
    }

    public SQL Postgres() {
        return new SQL("postgres", this.config);
    }

    public void call(String engineName, String funcName, int timeout, Object params, Utils.ResponseListener listener) {
        HashMap map = new HashMap<>();
        map.put("timeout", timeout);
        map.put("params", params);
        Utils.fetch(this.config.client, "post", this.config.token,
                this.config.url + "v1/functions/" + engineName + "/" + funcName,
                new Gson().toJson(map), listener);
    }
}
