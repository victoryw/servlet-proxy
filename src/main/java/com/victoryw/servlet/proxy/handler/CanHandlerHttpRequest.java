package com.victoryw.servlet.proxy.handler;

import javax.servlet.http.HttpServletRequest;

public interface CanHandlerHttpRequest {
    boolean canHandle(HttpServletRequest httpServletRequest);
}
