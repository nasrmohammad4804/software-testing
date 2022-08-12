package com.nasr.softwaretesting.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Setter
@Getter
public class CardPaymentCharge {

    private final boolean isCardDebited;
}
