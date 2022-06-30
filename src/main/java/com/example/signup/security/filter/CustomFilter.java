package com.example.signup.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.signup.dto.PayloadDto;
import com.example.signup.exceptions.BasicException;
import com.example.signup.security.Jwt;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.RemoteEndpoint;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@Slf4j
public class CustomFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public CustomFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("emailId");
        String password = request.getParameter("password");
        String regex = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$";
        if (Pattern.compile(regex).matcher(username).matches()) {
            username = username;
        }
        else{
            Map<String,String> errorResponse = new HashMap<>();
            errorResponse.put("error","Not a valid email");
            response.setStatus(200);
            response.setContentType(APPLICATION_JSON_VALUE);
            try {
                new ObjectMapper().writeValue(response.getOutputStream(), errorResponse);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);


    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        if(request.getParameter("grant_type").equals("password")) {
            User user = (User) authResult.getPrincipal();
            String access_token = Jwt.generateAccessToken(user, request.getRequestURI().toString());
            String refresh_token = Jwt.generateRefreshToken(user, request.getRequestURI().toString());
            Map<String, String> token = new LinkedHashMap<>();
            token.put("userName", user.getUsername());
            token.put("access", access_token);
            token.put("refresh", refresh_token);
            token.put("expires-on", "5");
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), token);
        }
        else if(request.getParameter("grant_type").equals("refresh_token")){
            User user = (User) authResult.getPrincipal();
            String token = request.getParameter("refresh_token");
            String username = Jwt.decodeRefreshToken(token);
            if(user.getUsername().equals(username)){
                String access_token = Jwt.generateAccessToken(user, request.getRequestURI().toString());
                Map<String, String> tokens = new LinkedHashMap<>();
                tokens.put("userName", user.getUsername());
                tokens.put("access", access_token);
                tokens.put("expires-on", "-1");
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), token);
            }
        }
        else{
            Map<String, String> responser = new LinkedHashMap<>();
            responser.put("message", "Not a valid token");
            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getOutputStream(), responser);
        }

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String, String> responseMessage = new LinkedHashMap<>();
        responseMessage.put("message", "Not a valid User");
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), responseMessage);
    }
}
