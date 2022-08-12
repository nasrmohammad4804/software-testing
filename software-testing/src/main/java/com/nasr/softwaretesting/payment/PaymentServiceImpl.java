package com.nasr.softwaretesting.payment;

import com.nasr.softwaretesting.customer.Customer;
import com.nasr.softwaretesting.customer.CustomerService;
import com.nasr.softwaretesting.sms.SmsRequest;
import com.nasr.softwaretesting.sms.SmsSender;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    @Autowired
    private final   CustomerService customerService;

    private static final List<Currency> acceptedCurrency=List.of(Currency.GBP,Currency.USD);

    @Autowired
    private final SmsSender smsSender;

    @Autowired
    private final PaymentRepository repository;

    @Autowired
    private final CardPaymentCharger cardPaymentCharger;

    @Override
    public void chargeCard(String customerId , PaymentModel model) throws IOException {

        Customer customer = customerService.getById(customerId);

        boolean supportCurrency = acceptedCurrency.stream().anyMatch(currency -> currency.equals(model.getPayment().getCurrency()));

        if (!supportCurrency)
            throw new IllegalStateException("this currency not supported");

        Payment payment = model.getPayment();
        CardPaymentCharge chargeCard = cardPaymentCharger
                .chargeCard(payment.getAmount(), payment.getCurrency(), payment.getSource(), payment.getDescription());

        if (!chargeCard.isCardDebited())
            throw new IllegalStateException("card not debited from customer "+customerId);

        payment.setCustomer(customer);
        repository.save(payment);

        smsSender.send(new SmsRequest(customer.getPhoneNumber(),true,"hi , your account is charged  with "+model.getPayment().getAmount()));
    }
}
