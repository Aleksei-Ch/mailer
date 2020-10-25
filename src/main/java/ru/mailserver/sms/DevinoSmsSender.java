package ru.mailserver.sms;

import com.devinotele.smsservice.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import ru.mailserver.model.SmsRequest;

import java.time.Instant;

public class DevinoSmsSender extends WebServiceGatewaySupport implements SmsSender {

    @Value("${sms.user}")
    private String user;

    @Value("${sms.password}")
    private String password;

    @Value("${sms.from}")
    private String from;

    private String getSessionId() {

        var sessionIdReq = new GetSessionID();
        sessionIdReq.setLogin(user);
        sessionIdReq.setPassword(password);

        var sessionIdResp = (GetSessionIDResponse) getWebServiceTemplate().marshalSendAndReceive(sessionIdReq,
                new SoapActionCallback("http://ws.devinosms.com/GetSessionID"));

        return sessionIdResp.getGetSessionIDResult();

    }

    public String send(SmsRequest req) throws Exception {

        var sessionId = getSessionId();

        var smsReq = new SendMessageByTimeZone();
        smsReq.setSessionID(sessionId);
        smsReq.setSourceAddress(from);
        smsReq.setDestinationAddress(req.getTo());
        smsReq.setData(req.getBody());
        smsReq.setSendDate(Instant.now());
        smsReq.setValidity(1);

        var smsResp = (SendMessageByTimeZoneResponse) getWebServiceTemplate().marshalSendAndReceive(smsReq,
                new SoapActionCallback("http://ws.devinosms.com/SendMessageByTimeZone"));

        if (smsResp == null ||
                smsResp.getSendMessageByTimeZoneResult() == null ||
                smsResp.getSendMessageByTimeZoneResult().getString() == null ||
                smsResp.getSendMessageByTimeZoneResult().getString().size() == 0
        ) {
            throw new Exception("Something went wrong when sending SMS");
        }

        return smsResp.getSendMessageByTimeZoneResult().getString().get(0);

    }

    public String getStatus(String smsId) throws Exception {

        var sessionId = getSessionId();

        var statusReq = new GetMessageState();
        statusReq.setSessionID(sessionId);
        statusReq.setMessageID(Long.parseLong(smsId));

        var statusResp = (GetMessageStateResponse) getWebServiceTemplate().marshalSendAndReceive(statusReq,
                new SoapActionCallback("http://ws.devinosms.com/GetMessageState"));

        if (statusResp == null ||
                statusResp.getGetMessageStateResult() == null) {
            throw new Exception("Something went wrong when getting SMS ID");
        }

        return statusResp.getGetMessageStateResult().getStateDescription();
    }

}
