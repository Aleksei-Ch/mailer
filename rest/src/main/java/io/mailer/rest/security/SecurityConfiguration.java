package io.mailer.rest.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import io.mailer.rest.data.ApiKey;
import io.mailer.rest.data.repo.ApiKeyRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private ApiKeyRepository apiRepo;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        var filter = new APIKeyFilter();
        filter.setAuthenticationManager(new AuthenticationManager() {

            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                
                String key = (String) authentication.getPrincipal();
                if (key == null) {
                    throw new BadCredentialsException("The API key is not presented.");
                }

                var dbRes = apiRepo.findByApiKeyAndEnabled(key, ApiKey.Enabled.Y);
                if (dbRes == null || dbRes.isEmpty())
                {
                    throw new BadCredentialsException("The API key is incorrect.");
                }
                
                authentication.setAuthenticated(true);
                return authentication;
            }
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