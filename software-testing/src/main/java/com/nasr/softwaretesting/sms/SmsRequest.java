package com.nasr.softwaretesting.sms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SmsRequest {

    private String to;
    private boolean isFlash;
    private String text;
}
