package com.lujieni.zuul.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;

/**
 * @Package: com.lujieni.zuul.config
 * @ClassName: MyFilter
 * @Author: lujieni
 * @Description: 1
 * @Date: 2021-02-18 16:43
 * @Version: 1.0
 */
public class MyFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext requestContext = RequestContext.getCurrentContext();
        requestContext.setSendZuulResponse(false);
        return false;//返回false代表当前filter的run方法不会执行,但后面的filter仍旧会执行
    }

    @Override
    public Object run() throws ZuulException {
        return null;
    }
}