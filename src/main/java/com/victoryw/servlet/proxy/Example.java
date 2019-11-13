package com.victoryw.servlet.proxy;

import com.victoryw.servlet.proxy.http.client.OkHttpClientBuilder;
import com.victoryw.servlet.proxy.http.proxies.HttpProxy;
import com.victoryw.servlet.proxy.split.SplitCanHandler;
import com.victoryw.servlet.proxy.transform.core.ProxyAddress;
import com.victoryw.servlet.proxy.split.SplitProxyRequestTransformer;
import com.victoryw.servlet.proxy.split.SplitProxyResponseTransformer;

public class Example {
    public static void main(String[] args) {
        final ProxyAddress proxyAddress = new ProxyAddress("http://abc.com/");
        HttpProxy splitProxy = new HttpProxy(
                new SplitProxyRequestTransformer(proxyAddress),
                new SplitProxyResponseTransformer(proxyAddress),
                new SplitCanHandler(),
                OkHttpClientBuilder.createOkHttpClient()
        );

        final ProxyCluster cluster = new ProxyCluster(splitProxy);
//        cluster.Proxy(request, response);
    }
}
