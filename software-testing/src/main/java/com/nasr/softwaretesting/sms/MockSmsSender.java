package com.nasr.softwaretesting.sms;

import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@ConditionalOnProperty(value = "enable.sms-sender",havingValue = "false")
public class MockSmsSender implements SmsSender {

    @Override
    public Response send(SmsRequest request) throws IOException {

        return  new Response.Builder().message("message sent")
                .request(new Request.Builder().url("http://localhost:9090/api/v1/payment").build())
                .protocol(Protocol.HTTP_1_0)
                .code(1).build();
    }
}
