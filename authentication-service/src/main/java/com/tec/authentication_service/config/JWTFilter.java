package com.tec.authentication_service.config;

import com.tec.authentication_service.service.MyUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Service
public class JWTFilter extends OncePerRequestFilter {

    private static final Logger log = LogManager.getLogger(JWTFilter.class);
    @Autowired
    JwtTokenService jwtTokenService;

    @Autowired
    ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String requestHeader = request.getHeader("Authorization");
        String token = null;

        String username = null;

        System.out.println("::::In JWT Filter");
        if(requestHeader != null && requestHeader.startsWith("Bearer ")) {

            token = requestHeader.substring(7);
            System.out.println("Token received::: "+token);
            try{
                username = jwtTokenService.extractUserName(token);

            }catch (ExpiredJwtException expiredJwtException){
                response.setStatus(412);
                response.getWriter().write("The JWT token has expired");
                System.out.println("Token expired");
                log.error("Exception::::expiredJwtException : ", expiredJwtException);
                return;

            }catch (Exception e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT validation failed");
                System.out.println("Other JWT validation exception");
                log.error("e: ",e);
                return;

            }
        }

        System.out.println("Username from extractUserNameFromToken" + username);

        if (username != null  && SecurityContextHolder.getContext().getAuthentication()==null) {

            System.out.println("Username in JWT filter" + username);
            UserDetails userDetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);


            try {
                if (jwtTokenService.validateToken(token, userDetails)) {

                    /*
                    This below code snippet manually sets an authenticated user into the Spring Security context, typically after verifying a JWT token. It bypasses the default login mechanism and tells Spring Security:
                    “This user is authenticated, and here are their roles. Trust them for this request.”
                     */
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }else{
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("JWT validation failed");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("JWT validation exception: " + e.getMessage());
                return;
            }

        }


        filterChain.doFilter(request,response);

    }
}
