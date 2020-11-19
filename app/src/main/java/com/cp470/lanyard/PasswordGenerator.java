package com.cp470.lanyard;

import java.security.SecureRandom;
import java.util.ArrayList;

public class PasswordGenerator {

    final static String uppercaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final static String lowercaseChars = "abcdefghijklmnopqrstuvwxyz";
    final static String symbolChars = "!@#$%^&*_-=+|:;,.<>/?";
    final static String numberChars = "1234567890";

    public static String generate(Boolean upperChecked, Boolean lowerChecked, Boolean symbolChecked, Boolean numberChecked, int stringLength){
        if((!upperChecked && !lowerChecked && !symbolChecked && !numberChecked)||stringLength<1){
            return ""; //catch bad input, should otherwise be handled by calling functions beforehand or after this return
        }
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(stringLength);

        //add strings of password chars to a list based on whether they were selected by User
        ArrayList<String> charSets=new ArrayList<String>();
        if(upperChecked){charSets.add(uppercaseChars);}
        if(lowerChecked){charSets.add(lowercaseChars);}
        if(symbolChecked){charSets.add(symbolChars);}
        if(numberChecked){charSets.add(numberChars);}
        int choice = 0;
        String tempCharSet="";
        //Random string generation ""Algorithm""
        //given the four reference strings, the user could choose any combination
        //so the strings in use (that is, the ones the user checked in settings) are added to an array,
        // the loop picks a random one from the array, then picks a random letter from that string
        for(int i=0;i<stringLength;++i){
            //Choice is number from 0-3 or less, depending on how many settings there are
            choice = random.nextInt(charSets.size());
            tempCharSet=charSets.get(choice); //the currently picked string, could be any of upperCaseChars, lowerCaseChars,etc
            sb.append(tempCharSet.charAt(random.nextInt(tempCharSet.length())));
        }
        return sb.toString();
    }
}
