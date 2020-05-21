package io.mailer.rest.security;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

public class APIKeyFilter extends AbstractPreAuthenticatedProcessingFilter {

    private static final Logger LOG = LoggerFactory.getLogger(APIKeyFilter.class);
    private static final ObjectMapper mapper = new ObjectMapper();

    private static final String API_KEY = "api_key";

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {
        String key = null;
        try {
            var map = mapper.readValue(
                request.getReader()
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator())), 
                Map.class);
            key = (String) map.get(API_KEY);
        } catch (IOException e) {
            LOG.error("Unexpected error: ", e);
        }
        return key;
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {
        return null;
    }

}