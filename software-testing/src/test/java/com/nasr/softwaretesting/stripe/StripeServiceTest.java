package com.nasr.softwaretesting.stripe;

import com.nasr.softwaretesting.payment.CardPaymentCharge;
import com.nasr.softwaretesting.payment.Currency;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class StripeServiceTest {


    private StripeService underTest;

    @BeforeEach
    void setUp() {
        underTest = new StripeService();
    }

    @Test
    void itShouldChargeCard() {
        // given
        BigDecimal amount = new BigDecimal("40");
        Currency currency = Currency.USD;
        String source = "tok_visa";
        String description = "buying microservice course";

        Charge charge= new Charge();
        charge.setPaid(true);

        try (MockedStatic<Charge> mockStatic = mockStatic(Charge.class)) {


            mockStatic.when(() -> Charge.create(any(), any(RequestOptions.class))).thenReturn(charge);

            //unit test must be isolated then never call external api or service we should mock external api
            // when
            CardPaymentCharge cardPaymentCharge = underTest.chargeCard(amount, currency, source, description);

            //then
            assertThat(cardPaymentCharge.isCardDebited())
                    .isTrue();
        }

    }

    @Test
    void itShouldThrowWhenConnectingWithStripeApi()  {
        // given
        BigDecimal amount = new BigDecimal("40");
        Currency currency = Currency.USD;
        String source = "tok_visa";
        String description = "buying microservice course";

        // when
        //then
        try(MockedStatic<Charge> mockedStatic = mockStatic(Charge.class)) {
            mockedStatic.when(() ->Charge.create(any(),any(RequestOptions.class))).thenThrow(new InvalidRequestException("your request not valid",null,null,null,null));

            assertThatThrownBy( () -> underTest.chargeCard(amount,currency,source,description))
                    .isInstanceOf(IllegalStateException.class);
        }
    }
}