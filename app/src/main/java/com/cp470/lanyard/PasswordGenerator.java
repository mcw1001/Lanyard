package com.cp470.lanyard;

import java.security.SecureRandom;
import java.util.ArrayList;

public class PasswordGenerator {

    final static String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final static String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
    final static String symbolChars = "!@#$%^&*()_-=+{}|:;,.<>/?";
    final static String numberChars = "1234567890";

    public static String generate(Boolean isUp, Boolean isLow, Boolean isSymbol, Boolean isNum, int stringLength){
        if((!isUp && !isLow && !isSymbol && !isNum)||stringLength<1){
            return "";
        }
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(stringLength);
        String charSet = "";
        ArrayList<String> charSets=new ArrayList<String>();
        if(isUp){charSets.add(uppercaseChars);}
        if(isLow){charSets.add(lowercaseChars);}
        if(isSymbol){charSets.add(symbolChars);}
        if(isNum){charSets.add(numberChars);}
        int choice = 0;
        String tempCharSet="";
        for(int i=0;i<stringLength;++i){
            choice = random.nextInt(charSets.size());
            tempCharSet=charSets.get(choice);
            sb.append(tempCharSet.charAt(random.nextInt(tempCharSet.length())));
        }
        return sb.toString();
    }
}
