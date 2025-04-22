package com.xvpi.smarttransportbackend.config;

import com.xvpi.smarttransportbackend.entity.TrafficOfficer;
import com.xvpi.smarttransportbackend.repository.TrafficOfficerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private TrafficOfficerRepository officerRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String token = jwtTokenUtil.resolveToken(request);

        if (token != null) {
            try {
                String username = jwtTokenUtil.getUsername(token);
                Optional<TrafficOfficer> optionalOfficer = officerRepository.findByUsername(username);

                if (optionalOfficer.isPresent()) {
                    TrafficOfficer officer = optionalOfficer.get();

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(officer, null, new ArrayList<>());

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }

            } catch (Exception e) {
                // token 无效或解析失败
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

