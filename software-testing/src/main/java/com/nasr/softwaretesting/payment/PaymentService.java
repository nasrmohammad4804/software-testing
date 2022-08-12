package com.nasr.softwaretesting.payment;

import java.io.IOException;

public interface PaymentService {

    void chargeCard(String customerId ,PaymentModel model) throws IOException;
}
