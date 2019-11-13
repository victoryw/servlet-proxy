package com.victoryw.servlet.proxy.http.client;

import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class OkHttpClientBuilder {

    public static OkHttpClient createOkHttpClient() {
        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(1000, TimeUnit.SECONDS)
                .writeTimeout(1000, TimeUnit.SECONDS)
                .readTimeout(1000, TimeUnit.SECONDS)
                .retryOnConnectionFailure(false)
                .followRedirects(false)
                .build();
        client.dispatcher().setMaxRequestsPerHost(1000);
        client.dispatcher().setMaxRequests(1000);
        return client;
    }
}
