package com.nasr.softwaretesting.stripe;

import com.nasr.softwaretesting.payment.CardPaymentCharge;
import com.nasr.softwaretesting.payment.CardPaymentCharger;
import com.nasr.softwaretesting.payment.Currency;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@ConditionalOnProperty(value = "stripe.enable",havingValue = "false")
public class MockStripeService implements CardPaymentCharger {

    @Override
    public CardPaymentCharge chargeCard(BigDecimal amount, Currency currency, String source, String description) {

        return new CardPaymentCharge(true);
    }
}
