package com.nasr.softwaretesting.payment;

import com.nasr.softwaretesting.customer.Customer;
import com.nasr.softwaretesting.customer.CustomerService;
import com.nasr.softwaretesting.sms.SmsSender;
import com.squareup.okhttp.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    private PaymentService underTest;

    @Mock
    private CustomerService customerService;

    @Mock
    private CardPaymentCharger cardPaymentCharger;

    @Mock
    private SmsSender smsSender;

    @Captor
    private ArgumentCaptor<Payment> captor;

    @BeforeEach
    void setUp() {
        underTest = new PaymentServiceImpl(customerService,smsSender, paymentRepository, cardPaymentCharger);
    }

    @Test
    void itShouldNotChargeCardWhenCustomerNotExists() {
        // given
        String customerId = "mad41cl51x";
        Payment payment = Payment.builder().source("card123")
                .description("buying microservice course")
                .currency(Currency.USD)
                .build();

        PaymentModel model = new PaymentModel(payment);
        when(customerService.getById(any())).thenThrow(new NoSuchElementException());

        // when
        //then
        assertThatThrownBy(() -> underTest.chargeCard(customerId, model))
                .isInstanceOf(NoSuchElementException.class);

        then(cardPaymentCharger).shouldHaveNoInteractions();
        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void itShouldNotChargeWhenCurrencyNotSupported() {
        // given
        String customerId = "mad41cl51x";
        Payment payment = Payment.builder().source("card123")
                .description("buying microservice course")
                .currency(Currency.EUR)
                .build();

        PaymentModel model = new PaymentModel(payment);
        when(customerService.getById(customerId)).thenReturn(mock(Customer.class));

        // when
        //then
        assertThatThrownBy(() -> underTest.chargeCard(customerId, model))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("this currency not supported");

        then(cardPaymentCharger).shouldHaveNoInteractions();
        then(paymentRepository).shouldHaveNoInteractions(); // it means then(paymentRepository).should(never()).chargeCard(any(),any());
        then(smsSender).shouldHaveNoInteractions();
    }

    @Test
    void itShouldChargeCardSuccessfully() throws IOException {
        // given
        String customerId = "mad41cl51x";
        Payment payment = Payment.builder().source("card123")
                .description("buying microservice course")
                .currency(Currency.USD)
                .build();


        //customer exists ...
        PaymentModel model = new PaymentModel(payment);
        when(customerService.getById(customerId)).thenReturn(mock(Customer.class));

        // card debited is true ...
        when(cardPaymentCharger.chargeCard(any(), any(), any(), any()))
                .thenReturn(new CardPaymentCharge(true));
        when(smsSender.send(any())).thenReturn(mock(Response.class));

        // when
        underTest.chargeCard(customerId, model);

        //then

        then(paymentRepository).should().save(captor.capture());

        verify(cardPaymentCharger, times(1))
                .chargeCard(payment.getAmount(), payment.getCurrency(), payment.getSource(), payment.getDescription());

        assertThat(captor.getValue()).isEqualTo(payment);
        assertThat(captor.getValue().getCustomer())
                .isNotNull();

        verify(smsSender,times(1)).send(any());

    }

    @Test
    void itShouldThrowWhenCardDoesntCharge() {
        // given
        String customerId = "vc123xm6";
        Payment payment = Payment.builder()
                .amount(new BigDecimal("40"))
                .currency(Currency.GBP)
                .source("card234")
                .description("buying microservice course")
                .build();

        PaymentModel model = new PaymentModel(payment);
        when(customerService.getById(customerId)).thenReturn(mock(Customer.class));
        when(cardPaymentCharger.chargeCard(any(), any(), any(), any()))
                .thenReturn(new CardPaymentCharge(false));


        // when
        //then
        assertThatThrownBy(() -> underTest.chargeCard(customerId, model))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("card not debited from customer " + customerId);

        then(paymentRepository).should(never()).save(payment);
    }
}