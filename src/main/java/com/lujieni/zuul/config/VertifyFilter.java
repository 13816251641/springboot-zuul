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
        /*
         * 这样的方法来做判断,如果这个请求最终被拦截掉,则后面的过滤器逻辑也不需要执行了
         *
         */
        if(!currentContext.sendZuulResponse()){
            return false;
        }
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        log.info("VertifyFilter-run");
        return null;
    }
}
