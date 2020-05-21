package io.mailer.rest.api.handler;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import io.mailer.rest.model.GlobalResponse;

@Component("restAuthenticationEntryPoint")
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint{

    private static final ObjectMapper mapper = new ObjectMapper();

    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authenticationException)
        throws IOException, ServletException {

        response.setContentType("application/json");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());

        var resp = GlobalResponse.builder()
            .timestamp(new Date())
            .status(HttpStatus.UNAUTHORIZED)
            .errors(List.of(authenticationException.getMessage()))
            .build();

        response.getOutputStream().println(mapper.writeValueAsString(resp));

    }
}