package com.victoryw.servlet.proxy.split;

import com.victoryw.servlet.proxy.transform.core.ProxyAddress;
import com.victoryw.servlet.proxy.transform.core.ProxyResponseTransformer;

import java.util.List;

public class SplitProxyResponseTransformer extends ProxyResponseTransformer {

    public SplitProxyResponseTransformer(ProxyAddress proxyAddress) {
        super(proxyAddress);
    }

    @Override
    protected String locationValue(List<String> headerValue, String proxyUrl, String value) {
        final String locationAddress = headerValue.get(0);
        if (locationAddress.startsWith(proxyUrl)) {
            value = locationAddress.split(proxyUrl)[1];
        }
        return value;
    }
}
