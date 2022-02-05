package com.example.zuulservice.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
public class ZuulLoggingFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre"; //사전 필터 사후 필터 인지 정함
    }

    @Override
    public int filterOrder() {
        return 1;//필터 실행 순서
    }

    @Override
    public boolean shouldFilter() {
        return true; //필터 사용 여부 결정
    }

    @Override //실제 동작을 지정
    public Object run() throws ZuulException {
        log.info("*************************** printing logs: ");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        log.info("*************************** "+request.getRequestURI());
        return null;
    }
    
}
