package com.cp470.lanyard;

import java.security.SecureRandom;

public class PasswordGenerator {

    final static String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final static String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
    final static String symbolChars = "!@#$%^&*()_-=+{}|:;,.<>/?";
    final static String numberChars = "1234567890";

    public static String generate(Boolean isUp, Boolean isLow, Boolean isSymbol, Boolean isNum, int stringLength){
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(stringLength);
        String charSet = "";
        if(isUp){charSet+=uppercaseChars;}
        if(isLow){charSet+=lowercaseChars;}
        if(isSymbol){charSet+=symbolChars;}
        if(isNum){charSet+=numberChars;}

        for(int i=0;i<stringLength;++i){
            sb.append(charSet.charAt(random.nextInt(charSet.length())));
        }
        //TODO finer random num generation including password minimums (at least 2 chars, at least 1 uppercase letter)
        return sb.toString();
    }
}
