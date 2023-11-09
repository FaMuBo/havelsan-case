package com.havelsan.backend.config;

import com.havelsan.backend.security.jwt.AuthEntryPointJwt;
import com.havelsan.backend.security.jwt.AuthTokenFilter;
import com.havelsan.backend.security.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthTokenFilter authTokenFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, ObjectProvider<AuthEntryPointJwt> unauthorizedHandler) throws Exception {
        http.cors(Customizer.withDefaults());
        //http.cors();
        http.csrf(AbstractHttpConfigurer::disable);
        http.exceptionHandling(hshec -> hshec.authenticationEntryPoint(unauthorizedHandler.getIfAvailable()));
        http.sessionManagement(hssmc -> hssmc.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.authorizeHttpRequests(cfg -> cfg
                .requestMatchers(antMatcher("/user/login"), antMatcher("/h2-console/**"), antMatcher("/user/register")).permitAll()
                .anyRequest().authenticated()
        );
        http.headers(hshc -> hshc.frameOptions(foc-> foc.disable()));
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
