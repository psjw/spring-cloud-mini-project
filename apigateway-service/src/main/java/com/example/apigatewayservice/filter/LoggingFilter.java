package com.example.apigatewayservice.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
//라우팅 정보에 별도로 등록 필요
public class LoggingFilter extends AbstractGatewayFilterFactory<LoggingFilter.Config> {
    public LoggingFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        //Logging Pre Filter
        GatewayFilter filter = new OrderedGatewayFilter((exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            log.info("Logging filter baseMessage : -> {}", config.getBaseMessage());
            if(config.isPreLogger()){
                log.info("Logging PRE Filter : request id -> {}",request.getId());
            }

            //Logging Post Filter
            return chain.filter(exchange).then(Mono.fromRunnable(() -> {  //Mono 단일값 전달 비동기전송방식일때
                if(config.isPostLogger()){
                    log.info("Logging POST Filter : response code -> {}", response.getStatusCode());
                }
            }));
        }, Ordered.LOWEST_PRECEDENCE);  //Ordered.HIGHEST_PRECEDENCE 가장 먼저 실행, Ordered.LOWEST_PRECEDENCE 가장 나중에 실행

        return filter;
    }

    @Data
    public static class Config {
        //Put the configuration properties
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }
}
