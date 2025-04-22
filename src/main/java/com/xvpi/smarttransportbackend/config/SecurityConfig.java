package com.xvpi.smarttransportbackend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JWTFilter jwtTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors()  // ✅ 开启跨域支持，调用 CorsConfigurationSource
                .and()
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .antMatchers(
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/api/police/login",
                                "/api/police/register",
                                "/api/classify/**",
                                "/api/camera-device/**",
                                "/api/total-record/**",
                                "/api/screen/**",
                                "/command/**",
                                "/api/road-section/**",
                                "/api/predict/**",
                                "/admin/**"
                        ).permitAll()
                        .antMatchers("/api/police/me").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
