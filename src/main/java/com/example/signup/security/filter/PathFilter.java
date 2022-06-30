package com.example.signup.security.filter;

import com.example.signup.Utility.Utility;
import com.example.signup.dto.PayloadDto;
import com.example.signup.security.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static com.google.common.net.HttpHeaders.AUTHORIZATION;
import static java.util.Arrays.stream;

@Component
@Order(0)
@Slf4j
public class PathFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes())
                .getRequest();


        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getServletPath().equals("/authenticate")){
            filterChain.doFilter(request,response);
        }
        else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader !=null && authorizationHeader.startsWith("Bearer")){
                try {
                    String token = authorizationHeader.substring("Bearer".length());
                    PayloadDto dto = Jwt.decodeToken(token);
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

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
