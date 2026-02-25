package com.instagram.api_gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.HandlerFilterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.security.Key;
import java.util.Date;

@Component
public class JwtAuthFilter {

    @Value("${jwt.secret}")
    private String secretKey;

    public HandlerFilterFunction<ServerResponse, ServerResponse> apply() {
        return (request, next) -> {
            String authHeader = request.headers().firstHeader("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ServerResponse.status(401).build();
            }

            String token = authHeader.substring(7);

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(getSignKey())
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                if (claims.getExpiration().before(new Date())) {
                    return ServerResponse.status(401).build();
                }

                ServerRequest mutated = ServerRequest.from(request)
                        .header("X-User-Id", claims.get("userId").toString())
                        .header("X-Username", claims.get("username").toString())
                        .build();

                return next.handle(mutated);

            } catch (Exception e) {
                return ServerResponse.status(401).build();
            }
        };
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}