package org.example.tourservice.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
public class SecurityConfiguration {
    private final JwtAuthenticationFilter authFilter;

    @Autowired
    public SecurityConfiguration(JwtAuthenticationFilter authFilter) {
        this.authFilter = authFilter;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET,"/tours").permitAll()
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-resources/**",
                                "/swagger-ui.html",
                                "/webjars/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.GET,"/tours/destination").permitAll()
                        .requestMatchers(HttpMethod.GET,"/tours/region").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tours/filter").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tours/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tours/destination/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tours/{tourId}/check-availability").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tours/admin/all").hasAnyRole("ADMIN", "MODERATOR")
                        .requestMatchers(HttpMethod.POST, "/tours/create").hasAnyRole("ADMIN", "MODERATOR")
                        .requestMatchers(HttpMethod.PUT, "/tours/{id}").hasAnyRole("ADMIN", "MODERATOR")
                        .requestMatchers(HttpMethod.PUT, "/tours/{id}/delete").hasAnyRole("ADMIN", "MODERATOR")
                        .requestMatchers(org.springframework.http.HttpMethod.OPTIONS, "/**").permitAll()
                .anyRequest().authenticated())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
