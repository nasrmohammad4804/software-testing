package com.nasr.softwaretesting.sms;

import com.squareup.okhttp.Response;

import java.io.IOException;

public interface SmsSender {
    Response send(SmsRequest request) throws IOException;
}
