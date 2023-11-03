package com.hrproject.config.security;

import com.hrproject.utility.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenManager jwtTokenManager;
    private final JwtUserDetails jwtUserDetails;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        System.out.println(request.getRequestURL());

        String authorizationHeader = request.getHeader("Authorization");

        System.out.println(authorizationHeader);

        System.out.println("auth==>" + SecurityContextHolder.getContext().getAuthentication());

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")
            //   && SecurityContextHolder.getContext().getAuthentication()==null
        ) {
            String token = authorizationHeader.substring(7);
            Optional<Long> id = jwtTokenManager.getIdFromToken(token);
            UserDetails userDetails = null;
            System.out.println(token);
            if (id.isPresent()) {
                userDetails = jwtUserDetails.loadUserByUserId(id.get());
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            System.out.println("User==>" + userDetails);
            System.out.println("getcontext==>" + SecurityContextHolder.getContext());
        }

        // CORS başlıklarını burada ayarlayın
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000"); // İzin verilen origin
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
        response.setHeader("Access-Control-Allow-Credentials", "true");

        // Preflight isteğini ele alın
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            filterChain.doFilter(request, response);
        }
    }
}