package com.nasr.softwaretesting.stripe;

import com.nasr.softwaretesting.payment.CardPaymentCharge;
import com.nasr.softwaretesting.payment.CardPaymentCharger;
import com.nasr.softwaretesting.payment.Currency;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@ConditionalOnProperty(value = "stripe.enable",havingValue = "true")
public class StripeService implements CardPaymentCharger {

    private final static RequestOptions requestOptions = RequestOptions.builder()
            .setApiKey("sk_test_51L4UQlCg29g10epg9Orx8b0PxFkAIPOeJaIP2lQsCerkbFejajzN1NhvwBFAYt4cQwGcFcinUIOl5XZPqXhgT11u00a5XF2bXn")
            .build();

    @Override
    public CardPaymentCharge chargeCard(BigDecimal amount, Currency currency, String source, String description) {
        Map<String, Object> map = new HashMap<>();

        map.put("account", amount);
        map.put("currency", currency);
        map.put("source", source);
        map.put("description", description);

        try {
            Charge charge = Charge.create(map, requestOptions);
            return new CardPaymentCharge(charge.getPaid());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new IllegalStateException("can not make stripe charge");
        }
    }
}
