package com.victoryw.servlet.proxy.transform;

import okhttp3.Request;
import okhttp3.Response;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ProxyRequestTransformer {

    public abstract Request transform(HttpServletRequest httpServletRequest);
}


