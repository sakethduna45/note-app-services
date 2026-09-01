package com.tec.gateway.auth;

import com.tec.gateway.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class AuthenticationFilter implements GlobalFilter, Ordered{


    private final JWTService jwtService;

    public AuthenticationFilter(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("Authentication Filter Executed");

        String path = exchange.getRequest().getURI().getPath();
        System.out.println("Request Path: " + path);

        if(path.equals("/auth/login") || path.equals("/auth/register")){
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }else{
            String token = authHeader.substring(7);
            System.out.println("Token: " + token);

            if(!jwtService.validateToken(token)){
                System.out.println("Token is invalid");
                exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();

            }
        }

        return chain.filter(exchange);


    }

    @Override
    public int getOrder() {
        return -1;
    }
}
