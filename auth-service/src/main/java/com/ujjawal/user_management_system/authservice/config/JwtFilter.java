package com.ujjawal.user_management_system.authservice.config;

//import com.ujjawal.user_management_system.authservice.JwtService;
//import com.ujjawal.user_management_system.authservice.VerifyUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.springframework.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JwtFilter extends OncePerRequestFilter {

//    final
//    JwtService jwtService;
//
//    final
//    ApplicationContext context;

//    public JwtFilter(JwtService jwtService, ApplicationContext context) {
//        this.jwtService = jwtService;
//        this.context = context;
//    }

    @Override
    public void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,@NonNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String identifier = null;

//        if(authHeader != null && authHeader.startsWith("Bearer ")){
//            token = authHeader.substring(7);
//            identifier = jwtService.extractIdentifier(token);
//        }
        System.out.println("identifier: "+identifier);
        System.out.println("token: "+token);
//        if(identifier != null && SecurityContextHolder.getContext().getAuthentication()==null){

//            UserDetails userDetails = context.getBean(VerifyUserService.class).loadUserByUsername(identifier);
//
//            if(jwtService.validateToken(token, userDetails)){
//                UsernamePasswordAuthenticationToken authToken =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//        }
        filterChain.doFilter(request, response);
    }
}
