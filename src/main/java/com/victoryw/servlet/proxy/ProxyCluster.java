package com.victoryw.servlet.proxy;

import com.google.common.collect.ImmutableList;
import com.victoryw.servlet.proxy.http.proxies.IHttpProxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

public class ProxyCluster {

    private final List<IHttpProxy> proxies;

    public ProxyCluster(List<IHttpProxy> proxies) {
        this.proxies = ImmutableList.copyOf(proxies);
    }

    public void Proxy(HttpServletRequest httpServletRequest,
                      HttpServletResponse httpServletResponse) {
        final Optional<IHttpProxy> proxy = proxies.stream().
                filter(httpProxy -> httpProxy.canProxy(httpServletRequest)).
                findFirst();

        proxy.orElseThrow(() -> new NoProxyToUseException(httpServletRequest)).
                Proxy(httpServletRequest, httpServletResponse);
    }
}

