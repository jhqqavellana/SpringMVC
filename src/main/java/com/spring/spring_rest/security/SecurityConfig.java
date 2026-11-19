package com.spring.spring_rest.security;


import com.spring.spring_rest.repository.AuthRoleRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.spring.spring_rest.service.CustomUserDetailsService;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

  private final AuthRoleRepository authRoleRepository;
  private CustomUserDetailsService userDetailsService;
  

  public SecurityConfig(CustomUserDetailsService userDetailsService, AuthRoleRepository authRoleRepository){
    this.setUserDetailsService(userDetailsService);
    this.authRoleRepository = authRoleRepository;
  }

  private void setUserDetailsService(CustomUserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  public CustomUserDetailsService getUserDetailsService() {
    return userDetailsService;
  }

  @Bean
  BCryptPasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager
      (AuthenticationConfiguration authentication) throws Exception{
        return authentication.getAuthenticationManager();
      }
  

    @Bean
    SecurityFilterChain filterChain(HttpSecurity security)
            throws Exception {
        return security
                .cors(cors -> cors.configure(security))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/api/students/**").hasAnyAuthority("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }
}

