package com.lujieni.zuul.config;

import com.alibaba.fastjson.JSON;
import com.lujieni.zuul.define.Response;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @Auther lujieni
 * @Date 2020/5/19
 */
@Component
public class AuthFilter extends ZuulFilter {
    private Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public String filterType() {
        /*
           在路由之前进行过滤
         */
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        logger.info("AuthFilter-shouldFilter");
        /* 获取请求上下文 */
        RequestContext requestContext = RequestContext.getCurrentContext();
        /* 获取到request */
        HttpServletRequest request = requestContext.getRequest();
        String uri = request.getRequestURI();
        String user = request.getParameter("user");
        logger.info("===uri==={}", uri);
        /*
            /lujieni/wahaha/love/zuul且user入参为空
         */
       if("/lujieni/wahaha/love/zuul".equals(uri) && StringUtils.isEmpty(user)){
            /* false代表不往下级服务去转发请求,但下一个filter仍旧会执行哦!!! */
            requestContext.setSendZuulResponse(false);

            Response response = new Response();
            response.setSuccess(false);
            response.setErrorCode(Response.ERROR_REDIRECT);
            response.setResult("http://www.baidu.com");
            requestContext.setResponseBody(JSON.toJSONString(response));//结果转为json
            /* return false代表不需要过滤,不会执行后面的run方法 */
            return false;
        }
        return true;
    }

    /**
     * 过滤通过后要执行的方法
     * 只有shouldFilter方法返回true后run方法里的逻辑才会执行
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        logger.info("AuthFilter-run");
        /* 获取请求上下文 */
        RequestContext requestContext = RequestContext.getCurrentContext();
        Cookie cookie = new Cookie("age","30");
        cookie.setMaxAge(1800); //设置时效时间
        cookie.setDomain("localhost");//绑定域名
        cookie.setPath("/");
        requestContext.getResponse().addCookie(cookie);
        return null;
    }

    /**
     * 设置 400 无权限状态
     */
    private void setUnauthorizedResponse(RequestContext requestContext, String msg) {
        requestContext.setSendZuulResponse(false);
        requestContext.setResponseStatusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
