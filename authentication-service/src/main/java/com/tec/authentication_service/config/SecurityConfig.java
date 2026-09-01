package com.tec.authentication_service.config;

import com.tec.authentication_service.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity

public class SecurityConfig {

    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    JWTFilter jwtFilter;

    @Bean
    public AuthenticationProvider getAuthProvider(){

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        provider.setUserDetailsService(userDetailsService);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);
        provider.setPasswordEncoder(bCryptPasswordEncoder);

        return provider;

    }

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(customizer -> customizer.disable());

        http.authorizeHttpRequests(request->request
                .requestMatchers("/auth/login","/auth/register","/auth/refresh").permitAll()
                .anyRequest()
                .authenticated());

//           http.formLogin(Customizer.withDefaults());
        http.httpBasic(Customizer.withDefaults());
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }



}
