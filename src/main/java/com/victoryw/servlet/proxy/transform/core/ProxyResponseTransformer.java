package com.victoryw.servlet.proxy.transform.core;

import okhttp3.Headers;
import okhttp3.Response;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class ProxyResponseTransformer {
    private static final List<String> IGNORE_RESPONSE_HEADERS = Arrays.asList("CONNECTION", "KEEP-ALIVE", "PROXY-AUTHENTICATE", "PROXY-AUTHORIZATION",
            "TE", "TRAILERS", "TRANSFER-ENCODING", "UPGRADE");
    private static final String LOCATION_HEADERS = "location";
    private final String proxyAddressStr;

    public ProxyResponseTransformer(ProxyAddress proxyAddress) {
        this.proxyAddressStr = proxyAddress.url();
    }

    private static void copyBody(HttpServletResponse httpServletResponse, Response proxyResponse) throws IOException {
        if(proxyResponse.body()!=null){
            if (proxyResponse.body().contentType() != null) {
                httpServletResponse.setContentType(
                        Objects.requireNonNull(proxyResponse.body().contentType()).toString());
            }

            try (final InputStream bodyStream = proxyResponse.body().byteStream()) {
                IOUtils.copy(bodyStream, httpServletResponse.getOutputStream());
            }
        }
    }

    public void transform(HttpServletResponse httpServletResponse, Response proxyResponse) throws IOException {
        httpServletResponse.setStatus(proxyResponse.code());
        copyResponseHeaders(httpServletResponse, proxyResponse, proxyAddressStr);
        copyBody(httpServletResponse, proxyResponse);
    }

    private void copyResponseHeaders(HttpServletResponse target, Response from, String proxyUrl) {
        final Headers proxyHeaders = from.headers();

        final List<String> noHeapHeaders = proxyHeaders.names().
                stream().
                filter(name -> !IGNORE_RESPONSE_HEADERS.contains(name.toUpperCase())).
                collect(Collectors.toList());

        noHeapHeaders.
                forEach(name -> {
                    final List<String> headerValue = proxyHeaders.values(name);
                    target.addHeader(name, handleHeaderValue(name,headerValue,proxyUrl));
                });
    }

    private String handleHeaderValue(String name, List<String> headerValue, String proxyUrl){
        String value = String.join(",", headerValue);
        if (LOCATION_HEADERS.equals(name.toLowerCase())) {
            value = locationValue(headerValue, proxyUrl, value);
        }
        return value;
    }

    protected abstract String locationValue(List<String> headerValue, String proxyUrl, String value);
}
