package ru.mailserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@EnableWebMvc
public class SystemConfiguration {

    public SystemConfiguration(@Autowired DispatcherServlet dispatcherServlet,
                               @Value("${proxy.set}") boolean proxySet,
                               @Value("${proxy.host}") String proxyHost,
                               @Value("${proxy.port}") String proxyPort) {
        // For 404 handling
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);

        // Proxy
        if (proxySet) {
            System.setProperty("http.proxySet", "true");
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", proxyPort);
            System.setProperty("https.proxySet", "true");
            System.setProperty("https.proxyHost", proxyHost);
            System.setProperty("https.proxyPort", proxyPort);
        }

    }

    @Bean
    public InternalResourceViewResolver defaultViewResolver() {
        return new InternalResourceViewResolver();
    }

}
