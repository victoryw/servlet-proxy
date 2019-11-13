package com.victoryw.servlet.proxy.transform.core;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ProxyRequestTransformer {
    private static final List<String> IGNORE_REQUEST_HEADERS = Collections.singletonList("HOST");
    private static final String CONTENT_LENGTH = "Content-Length";
    protected final ProxyAddress proxyAddress;

    protected ProxyRequestTransformer(ProxyAddress proxyAddress) {
        this.proxyAddress = proxyAddress;
    }

    public Request transform(final HttpServletRequest httpServletRequest) throws IOException {
        final HttpUrl proxyUrl = createProxyUrl(httpServletRequest);
        final Request.Builder requestBuilder = new Request.
                Builder().
                url(proxyUrl);
        //headers
        final List<AbstractMap.SimpleEntry<String, String>> headers =
                headers(httpServletRequest);
        headers.forEach(pair -> requestBuilder.header(pair.getKey(), pair.getValue()));

        RequestBody body = transformToBody(httpServletRequest);

        requestBuilder.method(getMethod(httpServletRequest), body);

        return requestBuilder.build();
    }

    protected abstract HttpUrl createProxyUrl(HttpServletRequest httpServletRequest);

    private List<AbstractMap.SimpleEntry<String, String>> headers(HttpServletRequest httpServletRequest) {
        final List<AbstractMap.SimpleEntry<String, String>> headers = collectHeaders(httpServletRequest);

        //setXForwardedForHeader
        headers.add(new AbstractMap.SimpleEntry<>("X-Forwarded-For", httpServletRequest.getRemoteAddr()));
        headers.add(new AbstractMap.SimpleEntry<>("X-Forwarded-Proto", httpServletRequest.getScheme()));
        headers.add(new AbstractMap.SimpleEntry<>("X-Proxy-With-User", "true"));
        headers.add(new AbstractMap.SimpleEntry<>("Connection", "close"));
        customHeaders(headers, httpServletRequest);
        return headers;
    }

    protected abstract void customHeaders(List<AbstractMap.SimpleEntry<String, String>> headers, HttpServletRequest httpServletRequest);

    private List<AbstractMap.SimpleEntry<String, String>> collectHeaders(HttpServletRequest httpServletRequest) {
        final Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        return Collections.list(headerNames).
                stream().
                filter(name -> !IGNORE_REQUEST_HEADERS.contains(name.toUpperCase())).
                map(name -> new AbstractMap.SimpleEntry<>(name, httpServletRequest.getHeader(name))).
                collect(Collectors.toList());
    }

    private RequestBody transformToBody(HttpServletRequest request) throws IOException {
        final boolean isEmptyContent = request.getHeader(CONTENT_LENGTH) == null || Integer.parseInt(request.getHeader(CONTENT_LENGTH)) == 0;
        if (isEmptyContent) {
            return null;
        }
        String contentType = request.getContentType();
        MediaType mediaType = contentType == null ? null : MediaType.get(contentType);

        try (ServletInputStream input = request.getInputStream();
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            IOUtils.copy(input, output);
            byte[] requestBytes = output.toByteArray();
            return RequestBody.create(mediaType, requestBytes);
        }
    }

    private String getMethod(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getMethod();
    }
}
