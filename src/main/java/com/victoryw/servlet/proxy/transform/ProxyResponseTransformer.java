package com.victoryw.servlet.proxy.transform;

import okhttp3.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ProxyResponseTransformer {
    public abstract void transform(HttpServletResponse httpServletResponse, Response proxyResponse);
}
