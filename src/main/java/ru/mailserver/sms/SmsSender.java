package ru.mailserver.sms;

import ru.mailserver.model.SmsRequest;

public interface SmsSender {

    String send(SmsRequest req) throws Exception;

    String getStatus(String id) throws Exception;

}
