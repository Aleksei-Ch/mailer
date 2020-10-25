package ru.mailserver.security;

import org.apache.logging.log4j.util.Strings;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

public class APIKeyFilter extends AbstractPreAuthenticatedProcessingFilter {

    private static final Logger LOG = LoggerFactory.getLogger(APIKeyFilter.class);

    private static final String AUTH_FILE_DELIMITER = "\\t";

    private final String authFile;

    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTH_FORMAT = "^ApiKey\\s[^\\s]+$";

    public APIKeyFilter(String authFile) {
        this.authFile = authFile;
    }

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest request) {

        var keyHash = (String) getPreAuthenticatedCredentials(request);

        return keyHash == null ? null : checkAuth(keyHash);

    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest request) {

        var authHeader = request.getHeader(AUTHORIZATION);
        if (Strings.isBlank(authHeader)) {
            return null;
        }

        authHeader = new String(Base64.getDecoder().decode(authHeader));
        if (!authHeader.matches(AUTH_FORMAT)) {
            return null;
        }

        return DigestUtils.sha256Hex(authHeader.split("\\s")[1].getBytes(StandardCharsets.UTF_8)).toUpperCase();
    }

    private String checkAuth(String keyHash) {

        try (var scanner = new Scanner(new File(authFile))) {
            while (scanner.hasNextLine()) {
                var row = scanner.nextLine().split(AUTH_FILE_DELIMITER);
                if (row.length > 1 && keyHash.equals(row[1].toUpperCase())) {
                    return row[0];
                }
            }
        } catch (FileNotFoundException e) {
            LOG.error("Cannot read auth file: ", e);
        }

        return null;
    }

}