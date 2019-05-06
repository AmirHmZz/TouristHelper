package com.entezaar.thelperdemo.util;

public class StringUtil {

    public static String checkPhoneNumber (String phoneNumber) {
        if (phoneNumber==null) return null;
        if (!phoneNumber.matches("^[0-9]*$")) return null ;
        return phoneNumber;

        //TODO A func called "phoneNumberFixer" should be created
        //TODO More Standards should be set !
    }
}
