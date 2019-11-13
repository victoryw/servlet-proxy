package com.victoryw.servlet.proxy.split;

import com.victoryw.servlet.proxy.handler.CanHandlerHttpRequest;

import javax.servlet.http.HttpServletRequest;

public class SplitCanHandler implements CanHandlerHttpRequest {
    @Override
    public boolean canHandle(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getRequestURL().toString().startsWith("splitWebSite");
    }
}
