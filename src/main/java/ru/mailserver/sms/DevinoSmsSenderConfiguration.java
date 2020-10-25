package ru.mailserver.sms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

@Configuration
public class DevinoSmsSenderConfiguration {

    private static final String SERVICE_URL = "http://ws-wsdl.devinotele.com/smsservice.asmx";
    private static final String SERVICE_PKG = "com.devinotele.smsservice";

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath(SERVICE_PKG);
        return marshaller;
    }

    @Bean
    public SmsSender smsSender(Jaxb2Marshaller marshaller) {
        DevinoSmsSender client = new DevinoSmsSender();
        client.setDefaultUri(SERVICE_URL);
        client.setMarshaller(marshaller);
        client.setUnmarshaller(marshaller);
        return client;
    }

}
