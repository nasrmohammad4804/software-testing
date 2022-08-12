package com.nasr.softwaretesting.utils;

public class PhoneNumberValidator {

    public static boolean validate(String phoneNumber) {

        for (Character character : phoneNumber.toCharArray())
            if (!Character.isDigit(character))
                return false;

        return phoneNumber.startsWith("09") && phoneNumber.length() == 11;
    }
}
