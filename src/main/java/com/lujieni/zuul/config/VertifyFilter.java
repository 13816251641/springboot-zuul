package com.lujieni.zuul.config;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

/**
 * @Auther lujieni
 * @Date 2020/7/1
 */
@Slf4j
@Component
public class VertifyFilter extends ZuulFilter {


    @Override
    public String filterType() {
       /*
           在路由之前进行过滤
         */
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 200;
    }

    @Override
    public boolean shouldFilter() {
        log.info("VertifyFilter-shouldFilter");
        RequestContext currentContext = RequestContext.getCurrentContext();
        if(!currentContext.sendZuulResponse()){
            return false;//本filter的run方法不会执行,但后面的filter仍旧会执行
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("VertifyFilter-run");
        return null;
    }
}
