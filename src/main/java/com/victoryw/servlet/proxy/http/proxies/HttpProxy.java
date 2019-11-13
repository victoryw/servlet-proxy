package com.victoryw.servlet.proxy.http.proxies;

import com.victoryw.servlet.proxy.transform.ProxyRequestTransformer;
import com.victoryw.servlet.proxy.transform.ProxyResponseTransformer;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HttpProxy {
//    private static final OkHttpClient HTTP_CLIENT = OkHttpClientBuilder.createOkHttpClient();
    private final ProxyRequestTransformer proxyRequestTransformer;
    private final ProxyResponseTransformer proxyResponseTransformer;
    private final CanHandlerHttpRequest canHandlerHttpRequest;
    private final OkHttpClient httpClient;

    public HttpProxy(ProxyRequestTransformer proxyRequestTransformer,
                     ProxyResponseTransformer proxyResponseTransformer,
                     CanHandlerHttpRequest canHandlerHttpRequest,
                     OkHttpClient httpClient) {
        this.proxyRequestTransformer = proxyRequestTransformer;
        this.proxyResponseTransformer = proxyResponseTransformer;
        this.canHandlerHttpRequest = canHandlerHttpRequest;
        this.httpClient = httpClient;
    }

    public boolean canProxy(HttpServletRequest httpServletRequest) {
        return canHandlerHttpRequest.can(httpServletRequest);
    }

    public void Proxy(HttpServletRequest httpServletRequest,
                      HttpServletResponse httpServletResponse) throws IOException {
        Request httpProxyRequest = proxyRequestTransformer.transform(httpServletRequest);
        try (final Response proxyResponse = httpClient.newCall(httpProxyRequest).execute()) {
            proxyResponseTransformer.transform(httpServletResponse, proxyResponse);
        }

    }
}

