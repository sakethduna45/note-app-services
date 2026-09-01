package com.tec.authentication_service.controller;


import com.tec.authentication_service.config.JwtTokenService;
import com.tec.authentication_service.model.AuthObject;
import com.tec.authentication_service.model.ResponseObject;
import com.tec.authentication_service.model.User;
import com.tec.authentication_service.repo.UserRepo;
import com.tec.authentication_service.service.MyUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")

public class AuthController {

    @Autowired
    private UserRepo repo;

    @Autowired
    MyUserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenService jwtTokenService;

    @GetMapping("/hello")
    public String hello(HttpServletRequest request){
        return "Hello world " + request.getSession().getId();
    }

    @GetMapping("/welcome")
    public String welcome(HttpServletRequest request){
        return "Welcome world " + request.getSession().getId();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        try {
            String username = jwtTokenService.extractUserNameFromRefreshToken(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtTokenService.validateTokenFromRefreshToken(refreshToken, userDetails)) {
                System.out.println("refresh token is valid ::: generating new access token");
                String newAccessToken = jwtTokenService.generateToken(username);
                return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired or invalid");
        }
    }

    @PostMapping("/welcome2")
    public String welcome2(HttpServletRequest request, @RequestBody String username){
        return "Welcome world2 " + request.getSession().getId() + " " + username;
    }



    @PostMapping("/login")
    public ResponseEntity<ResponseObject> loginWithUserNameAndPassword(@RequestBody AuthObject authObject){

        String token="";
        String refreshToken ="";
        System.out.println("Request Received from Android ::::: ");

        String userName = authObject.getUsername();
        String password = authObject.getPassword();

        System.out.println("Username received from request ::::: " + userName);
        System.out.println("Password received from request ::::: "+ password);

        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authObject.getUsername(), authObject.getPassword()));
            System.out.println("In Authentication "+authObject.getUsername()+ " "+authObject.getPassword());
            System.out.println("In Authentication ::: is authenticated:::"+ authentication.isAuthenticated() );

            if(authentication.isAuthenticated()){
                token = jwtTokenService.generateToken(userName);
                refreshToken = jwtTokenService.generateRefreshToken(userName);
            }

        } catch (Exception e) {
            System.out.println(e.toString());
        }

        ResponseObject rs = new ResponseObject(token,refreshToken,"request received success");

        if(userName != null && password != null){
            return new ResponseEntity<>(rs,HttpStatus.OK);
        }
        return  new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseObject> signUpWithUserNameAndPassword(@RequestBody AuthObject authObject){

        System.out.println("Request Received from Android ::::: ");

        String userName = authObject.getUsername();
        String password = authObject.getPassword();

        System.out.println("Username received from request ::::: " + userName);
        System.out.println("Password received from request ::::: "+ password);

        User user = new User(userName,password);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

        System.out.println("password before encoding" + user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println("password after encoding" + user.getPassword());

        repo.save(user);

        ResponseObject rs = new ResponseObject("","restoken124","request received success");

        if(userName != null && password != null){
            return new ResponseEntity<>(rs,HttpStatus.OK);
        }
        return  new ResponseEntity<>(null,HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
