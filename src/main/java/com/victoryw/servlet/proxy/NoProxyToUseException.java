package com.victoryw.servlet.proxy;

import javax.servlet.http.HttpServletRequest;

public class NoProxyToUseException extends RuntimeException {
    public NoProxyToUseException(HttpServletRequest request) {
        super(String.format("the request is %s no proxy to found", request.getRequestURI()));
    }
}
