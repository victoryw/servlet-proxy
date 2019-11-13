package com.victoryw.servlet.proxy.http.proxies;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IHttpProxy {
    boolean canProxy(HttpServletRequest httpServletRequest);
    void Proxy(HttpServletRequest httpServletRequest,
               HttpServletResponse httpServletResponse);
}
