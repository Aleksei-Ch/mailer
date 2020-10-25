package ru.mailserver.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Value("${authfile}")
    private String authFile;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfiguration(AuthenticationEntryPoint authenticationEntryPoint) {
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        var filter = new APIKeyFilter(authFile);
        filter.setAuthenticationManager(authentication -> {

            String keyHash = (String) authentication.getCredentials();
            if (keyHash == null) {
                throw new BadCredentialsException("The API key is not presented.");
            }

            var userName = (String) authentication.getPrincipal();
            if (userName == null)
            {
                throw new BadCredentialsException("The API key is incorrect.");
            }

            authentication.setAuthenticated(true);
            return authentication;
        });

        httpSecurity.
            antMatcher("/api/**")
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().addFilter(filter)
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }

}