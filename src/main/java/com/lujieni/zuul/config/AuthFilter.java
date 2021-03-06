package com.lujieni.zuul.config;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.lujieni.zuul.define.Response;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import javax.servlet.http.HttpServletRequest;
/**
 * @Auther lujieni
 * @Date 2020/5/19
 */
@Component
public class AuthFilter extends ZuulFilter {
    private final static Logger logger = LoggerFactory.getLogger(AuthFilter.class);

    @Override
    public String filterType() {
        /*
           在路由之前进行过滤
         */
        return FilterConstants.PRE_TYPE;
    }

    /**
     * @Description: 数字越小越先执行
     * @param
     * @return:
     * @Author: lujieni
     * @Date: 2021/2/18
     */
    @Override
    public int filterOrder() {
        return FilterConstants.PRE_DECORATION_FILTER_ORDER - 1;
    }

    public void verifyToken(String token) throws SignatureVerificationException {
        //String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0IHRva2VuIiwiYXVkIjoiYXBwIiwiZGVwdE5hbWUiOiJpdCIsImlzcyI6InNlcnZlciIsInVzZXJOYW1lIjoibHVqaWVuaTUyMCIsImFnZSI6IuiKs-m-hDE4In0.bEGCq5LnjIDRk8stzt_xsnL7q4q_3UuzoOtfJ5lAHrI";
        Algorithm algorithm = Algorithm.HMAC256("secret");
        /* reusable verifier instance */
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
    }

    @Override
    public boolean shouldFilter() {
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
        /* 获取到request */
        HttpServletRequest request = requestContext.getRequest();
        String uri = request.getRequestURI();
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){ //token都不传,打道回府
            requestContext.setSendZuulResponse(false);//这个请求最终不会被zuul转发到后端服务器
            Response response = new Response();
            response.setSuccess(false);
            response.setErrorCode(Response.ERROR_CODE_NOT_RIGHT_TO_ACCESS);
            response.setResult("need token!");
            requestContext.setResponseBody(JSON.toJSONString(response));//结果转为json
            return null;
        }else{
            try {
                verifyToken(token);//校验token
            } catch (Exception e) {
                logger.error("token非法",e);
                requestContext.setSendZuulResponse(false);//这个请求最终不会被zuul转发到后端服务器
                Response response = new Response();
                response.setSuccess(false);
                response.setErrorCode(Response.ERROR_CODE_NOT_RIGHT_TO_ACCESS);
                response.setResult("invalid token!");
                requestContext.setResponseBody(JSON.toJSONString(response));//结果转为json
                return null;
            }
        }
        String user = request.getParameter("user");
        logger.info("===uri==={}", uri);
        if("/lujieni/wahaha/love/zuul".equals(uri) && StringUtils.isEmpty(user)){
            /*
             * setSendZuulResponse(false)代表不往下级服务去转发请求,但下一个filter仍旧会执行哦!!!
             * 在这里程序会先执行:AuthFilter:shouldFilter -> VertifyFilter:shouldFilter -> VertifyFilter:run
             *
             */
            requestContext.setSendZuulResponse(false);

            Response response = new Response();
            response.setSuccess(false);
            response.setErrorCode(Response.ERROR_REDIRECT);
            response.setResult("by /lujieni/wahaha/love/zuul must has user parameter");
            requestContext.setResponseBody(JSON.toJSONString(response));//结果转为json
            /* return false不会执行本fliter的run方法 */
            return null;
        }
        return null;
    }
}
