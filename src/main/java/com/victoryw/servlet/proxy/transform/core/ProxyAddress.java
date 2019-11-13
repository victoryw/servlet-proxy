package com.victoryw.servlet.proxy.transform.core;

import okhttp3.HttpUrl;

public class ProxyAddress {

    private HttpUrl targetUrl;

    public ProxyAddress(String url) {
        targetUrl = HttpUrl.parse(url);
    }

    public String getScheme() {
        return targetUrl.scheme();
    }

    public String getHost() {
        return targetUrl.host();
    }

    public int getPort() {
        return targetUrl.port();
    }

    public String url() {
        return targetUrl.toString();
    }
}
