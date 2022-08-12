package com.nasr.softwaretesting.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class PhoneNumberValidatorTest {


    @ParameterizedTest
    //with @ValueSource we can pass only argument to test method each time and limitation of this annotation we didnt pass null as argument
    //if we want to pass null argument use @NullSource
    //when we want to pass input value with expected value we use @CsvSource
    @CsvSource(value = {"09132265490:true", "08544519073:false", "0902mn14590:false", "0945271890247:false"}, delimiter = ':')
    void itShouldValidatePhoneNumber(String phoneNumber, boolean expected) {
        // given
        // when
        boolean result = PhoneNumberValidator.validate(phoneNumber);
        //then
        assertThat(result).isEqualTo(expected);

    }
}
