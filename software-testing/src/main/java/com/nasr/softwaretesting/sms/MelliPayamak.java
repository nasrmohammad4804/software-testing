package com.nasr.softwaretesting.sms;

import com.squareup.okhttp.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@ConditionalOnProperty(value = "enable.sms-sender",havingValue = "true")
public class MelliPayamak implements SmsSender{

    private static final String USERNAME = "xx";
    private static final String PASSWORD = "xxx";
    @Override
    public Response send(SmsRequest smsRequest) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "username=" + USERNAME + "&password=" + PASSWORD + "&to="
                + smsRequest.getTo() + "&text=" + smsRequest.getText() + "&isflash=" + smsRequest.isFlash());

        Request request = new Request.Builder()
                /* don't get url and username and password for security problem*/
                .url("xxx")
                .post(body)
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "c26ca3b0-9f44-3cdf-9da3-60c86a9f75b3")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        return client.newCall(request).execute();
    }
}
