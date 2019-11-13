package com.victoryw.servlet.proxy;

import com.google.common.collect.ImmutableList;
import com.victoryw.servlet.proxy.http.proxies.HttpProxy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ProxyCluster {

    private final List<HttpProxy> proxies;

    public ProxyCluster(List<HttpProxy> proxies) {
        this.proxies = ImmutableList.copyOf(proxies);
    }

    public void Proxy(HttpServletRequest httpServletRequest,
                      HttpServletResponse httpServletResponse) throws IOException {
        final Optional<HttpProxy> proxy = proxies.stream().
                filter(httpProxy -> httpProxy.canProxy(httpServletRequest)).
                findFirst();

        proxy.orElseThrow(() -> new NoProxyToUseException(httpServletRequest)).
                Proxy(httpServletRequest, httpServletResponse);
    }
}

