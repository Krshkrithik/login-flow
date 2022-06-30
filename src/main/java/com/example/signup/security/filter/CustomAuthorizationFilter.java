package com.example.signup.security.filter;

import com.example.signup.dto.PayloadDto;
import com.example.signup.security.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static java.util.Arrays.stream;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().equals("/api/login")){
            filterChain.doFilter(request,response);
        }
        else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader !=null && authorizationHeader.startsWith("Bearer")){
                try {
                    String token = authorizationHeader.substring("Bearer".length());
                    PayloadDto dto = Jwt.decodeToken(token);
                    System.out.println(dto);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(dto.getRoles()).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken  = new UsernamePasswordAuthenticationToken(dto.getSubject(),null,authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request,response);
                }
                catch (Exception e){
                    log.error(e.getMessage());
                }
            }else{
                filterChain.doFilter(request,response);
            }
        }
    }
}
