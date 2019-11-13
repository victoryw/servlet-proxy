package com.victoryw.servlet.proxy.split;

import com.google.common.base.Strings;
import com.victoryw.servlet.proxy.transform.core.ProxyAddress;
import com.victoryw.servlet.proxy.transform.core.ProxyRequestTransformer;
import okhttp3.HttpUrl;

import javax.servlet.http.HttpServletRequest;
import java.util.AbstractMap;
import java.util.List;

public class SplitProxyRequestTransformer extends ProxyRequestTransformer {

    public SplitProxyRequestTransformer(ProxyAddress proxyAddress) {
        super(proxyAddress);
    }

    @Override
    protected HttpUrl createProxyUrl(HttpServletRequest httpServletRequest) {
        HttpUrl httpUrl = HttpUrl.parse(httpServletRequest.getRequestURL().toString());
        assert httpUrl != null;
        final ProxyAddress proxyAddress = this.proxyAddress;
        return new HttpUrl.Builder().
                scheme(proxyAddress.getScheme()).
                host(proxyAddress.getHost()).
                port(proxyAddress.getPort()).
                addEncodedPathSegments(String.join("/", httpUrl.encodedPathSegments())).
                encodedQuery(httpServletRequest.getQueryString()).
                build();
    }

    @Override
    protected void customHeaders(List<AbstractMap.SimpleEntry<String, String>> headers, HttpServletRequest httpServletRequest) {
        headers.add(new AbstractMap.SimpleEntry<>("X-Forwarded-Host-Core", getHostTarget(httpServletRequest)));
    }

    private String getHostTarget(HttpServletRequest httpServletRequest) {
        final HttpUrl.Builder builder = new HttpUrl.Builder().scheme(httpServletRequest.getScheme()).
                host(httpServletRequest.getServerName()).
                port(httpServletRequest.getServerPort());

        final String contextPath = httpServletRequest.getContextPath();
        if(!Strings.isNullOrEmpty(contextPath)) {
            builder.addEncodedPathSegment(contextPath.substring(1));
        }

        return builder.build().toString();
    }

}


