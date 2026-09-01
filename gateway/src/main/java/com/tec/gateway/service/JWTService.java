package com.tec.gateway.service;

import com.tec.gateway.util.JwtConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JWTService {


    @Value("${jwt.secret}")
    private String secretKey;



    public boolean validateToken(String token) {
        // Implement your JWT validation logic here
        try{
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token).getBody();
            return JwtConstants.ACCESS.equals(claims.get(JwtConstants.TYPE));
        }catch (Exception e){
            System.out.println(e.toString());
            return false;
        }


    }

    private Key getKey() {
        byte[] key = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(key);
    }

}
