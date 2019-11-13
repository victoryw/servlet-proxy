package com.victoryw.servlet.proxy;

import com.google.common.collect.ImmutableList;
import com.victoryw.servlet.proxy.http.proxies.IHttpProxy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProxyClusterTest {

    @Test
    void should_use_proxy_when_proxy_can_deal_the_request() {
        IHttpProxy mockProxy = mock(IHttpProxy.class);
        when(mockProxy.canProxy(any(HttpServletRequest.class))).thenReturn(true);
        ProxyCluster cluster = new ProxyCluster(ImmutableList.of(mockProxy));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        cluster.Proxy(request, response);
        verify(mockProxy).Proxy(request, response);
    }

    @Test
    void should_use_first_proxy_when_more_than_one_proxy() {
        IHttpProxy mockProxy = mock(IHttpProxy.class);
        when(mockProxy.canProxy(any(HttpServletRequest.class))).thenReturn(true);

        IHttpProxy mockProxy2 = mock(IHttpProxy.class);
        ProxyCluster cluster = new ProxyCluster(ImmutableList.of(mockProxy, mockProxy2));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        cluster.Proxy(request, response);
        verify(mockProxy).Proxy(request, response);

        verify(mockProxy2, never()).canProxy(request);
        verify(mockProxy2, never()).Proxy(request, response);
    }

    @Test
    void should_should_use_proxy_when_proxy_can_not_deal_the_request() {
        IHttpProxy mockProxy1 = mock(IHttpProxy.class);
        when(mockProxy1.canProxy(any(HttpServletRequest.class))).thenReturn(false);

        IHttpProxy mockProxy2 = mock(IHttpProxy.class);
        when(mockProxy2.canProxy(any(HttpServletRequest.class))).thenReturn(true);
        ProxyCluster cluster = new ProxyCluster(ImmutableList.of(mockProxy1, mockProxy2));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        cluster.Proxy(request, response);
        verify(mockProxy1, never()).Proxy(request, response);
    }

    @Test
    void should_throw_no_exception_when_no_proxy_can_deal_the_request() {
        IHttpProxy mockProxy = mock(IHttpProxy.class);
        when(mockProxy.canProxy(any(HttpServletRequest.class))).thenReturn(false);
        ProxyCluster cluster = new ProxyCluster(ImmutableList.of(mockProxy));

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Assertions.assertThrows(NoProxyToUseException.class, () -> {
            cluster.Proxy(request, response);
        });
    }
}