package com.nasr.softwaretesting.payment;

import java.math.BigDecimal;

public interface CardPaymentCharger {

    CardPaymentCharge  chargeCard(BigDecimal amount,Currency currency,String source,String description);
}