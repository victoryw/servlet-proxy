package com.victoryw.servlet.proxy.http.proxies;

import javax.servlet.http.HttpServletRequest;

public interface CanHandlerHttpRequest {
    boolean can(HttpServletRequest httpServletRequest);
}
