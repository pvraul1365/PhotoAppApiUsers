package net.javaguides.photoapp.api.users.security;

import lombok.RequiredArgsConstructor;
import net.javaguides.photoapp.api.users.service.UsersService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;

/**
 * WebSecurity
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 18/04/2026 - 17:40
 * @since 1.17
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurity {

    private final Environment environment;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UsersService usersService;
    private final BCryptPasswordEncoder bcCryptPasswordEncoder;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {

        // Configure AuthenticationManagerBuilder
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authenticationManagerBuilder.userDetailsService(usersService)
                .passwordEncoder(bcCryptPasswordEncoder);

        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // Create authentication filter
        AuthenticationFilter authenticationFilter =
                new AuthenticationFilter(usersService, environment, authenticationManager);
        authenticationFilter.setFilterProcessesUrl(environment.getProperty("login.url.path"));

        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers(HttpMethod.POST, "/users")
                        .access(new WebExpressionAuthorizationManager(
                                "hasIpAddress('" + environment.getProperty("gateway.ip") + "')"))
                        .requestMatchers(HttpMethod.GET, "/users/**")
                        .access(new WebExpressionAuthorizationManager(
                                "hasIpAddress('" + environment.getProperty("gateway.ip") + "')"))
                        .requestMatchers(HttpMethod.PUT, "/users/**")
                        .access(new WebExpressionAuthorizationManager(
                                "hasIpAddress('127.0.0.1') or hasIpAddress('0:0:0:0:0:0:0:1') or hasIpAddress('"
                                + environment.getProperty("gateway.ip") + "')"))
                        .requestMatchers(HttpMethod.DELETE, "/users/**")
                        .access(new WebExpressionAuthorizationManager(
                                "hasIpAddress('127.0.0.1') or hasIpAddress('0:0:0:0:0:0:0:1') or hasIpAddress('"
                                + environment.getProperty("gateway.ip") + "')"))
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/users/status/check").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                )
                .addFilter(authenticationFilter)
                .authenticationManager(authenticationManager)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin()));

        return http.build();
    }

}
