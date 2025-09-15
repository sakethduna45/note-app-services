package com.tec.authentication_service.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtTokenService {

    private static final String SECRET_KEY = "TmV3U2VjcmV0S2V5Rm9ySldUU2lnbmluZ1B1cnBvc2VzMTIzNDU2Nzg";
    private static final String REFRESH_TOKEN_SECRET_KEY = "TmV3U2VjcmV0S2V5Rm9ySldUU2lnbmluZ1B1cnBvc2Vzc2FrZXRo";


    // ACCESS TOKEN
    public String generateToken(String username){

        Map<String,Object> claims = new HashMap<>();

        claims.put("type","access");

        return Jwts
                .builder()

                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *2))
                .signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }


    private Key getKey(){
        byte[] key = Decoders.BASE64.decode(SECRET_KEY);

        return Keys.hmacShaKeyFor(key);
    }




    public String extractUserName(String token) {
        // extract the username from jwt token

            return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build().parseClaimsJws(token).getBody();
    }


    public boolean validateToken(String token, UserDetails userDetails) throws Exception{
            final String userName = extractUserName(token);
            return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }


    //REFRESH TOKEN
    public String generateRefreshToken(String username){

        Map<String,Object> claims = new HashMap<>();

        claims.put("type","refresh");

        return Jwts
                .builder()

                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 *15))
                .signWith(getRefreshKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getRefreshKey(){
        byte[] key = Decoders.BASE64.decode(REFRESH_TOKEN_SECRET_KEY);

        return Keys.hmacShaKeyFor(key);
    }

    public String extractUserNameFromRefreshToken(String token) {
        // extract the username from jwt token
            return extractClaimFromRefreshToken(token, Claims::getSubject);
    }

    private <T> T extractClaimFromRefreshToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaimsFromRefreshToken(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaimsFromRefreshToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getRefreshKey())
                .build().parseClaimsJws(token).getBody();
    }


    public boolean validateTokenFromRefreshToken(String token, UserDetails userDetails) {
            final String userName = extractUserNameFromRefreshToken(token);
            System.out.println("Extracted username from refresh token:::: " + userName);
            return (userName.equals(userDetails.getUsername()) && !isTokenExpiredFromRefreshToken(token));
    }

    private boolean isTokenExpiredFromRefreshToken(String token) {
        return extractExpirationFromRefreshToken(token).before(new Date());
    }

    private Date extractExpirationFromRefreshToken(String token) {
        return extractClaimFromRefreshToken(token, Claims::getExpiration);
    }

}
