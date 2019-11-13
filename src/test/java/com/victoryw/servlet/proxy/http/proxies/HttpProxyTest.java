package com.victoryw.servlet.proxy.http.proxies;

import com.victoryw.servlet.proxy.handler.CanHandlerHttpRequest;
import com.victoryw.servlet.proxy.transform.core.ProxyRequestTransformer;
import com.victoryw.servlet.proxy.transform.core.ProxyResponseTransformer;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class HttpProxyTest {
    @Test
    void should_pass_proxy_by_the_handler() {
        CanHandlerHttpRequest httpHandler = mock(CanHandlerHttpRequest.class);
        when(httpHandler.canHandle(any(HttpServletRequest.class))).thenReturn(true);
        final HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        final HttpProxy proxy = new HttpProxy(mock(ProxyRequestTransformer.class),
                mock(ProxyResponseTransformer.class),
                httpHandler,
                mock(OkHttpClient.class));
        assertTrue(proxy.canProxy(mockRequest));

        when(httpHandler.canHandle(any(HttpServletRequest.class))).thenReturn(false);
        HttpProxy proxy2 = new HttpProxy(mock(ProxyRequestTransformer.class),
                mock(ProxyResponseTransformer.class),
                httpHandler,
                mock(OkHttpClient.class));
        Assertions.assertFalse(proxy2.canProxy(mockRequest));
    }

    @Test
    void should_proxy_http_request_to_other() throws IOException {
        final ProxyRequestTransformer proxyRequestTransformer = mock(ProxyRequestTransformer.class);
        final ProxyResponseTransformer proxyResponseTransformer = mock(ProxyResponseTransformer.class);
        final OkHttpClient okHttpClient = mock(OkHttpClient.class);

        final HttpProxy httpProxy = new HttpProxy(proxyRequestTransformer,
                proxyResponseTransformer,
                mock(CanHandlerHttpRequest.class),
                okHttpClient);

        final HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        final HttpServletResponse mockResponse = mock(HttpServletResponse.class);
        final Request request = mock(Request.class);
        final Response response = mock(Response.class);
        final Call call = mock(Call.class);

        when(proxyRequestTransformer.transform(mockRequest)).thenReturn(request);
        when(okHttpClient.newCall(request)).thenReturn(call);
        when(call.execute()).thenReturn(response);

        httpProxy.Proxy(mockRequest, mockResponse);

        verify(proxyRequestTransformer).transform(mockRequest);
        verify(proxyResponseTransformer).transform(mockResponse, response);
    }
}