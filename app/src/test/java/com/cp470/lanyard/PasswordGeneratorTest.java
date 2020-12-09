package com.cp470.lanyard;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;

public class PasswordGeneratorTest {
    boolean upperChecked;
    boolean lowerChecked;
    boolean symbolChecked;
    boolean numberChecked;

    // TEST HELPER FUNCTIONS
    private boolean hasNumber(String string) {
        return string.matches("(.*)[0-9](.*)");
    }

    private boolean hasLowercase(String string) {
        return string.matches("(.*)[a-z](.*)");
    }

    private boolean hasUppercase(String string) {
        return string.matches("(.*)[A-Z](.*)");
    }

    private boolean hasSymbol(String string) {
        return string.matches("(.*)[$&+,:;=?@#|'<>.^*()%!-](.*)");
    }
    // END TEST HELPER FUNCTIONS

    @Test
    public void generateWithAllProperties() {
        upperChecked = true;
        lowerChecked = true;
        symbolChecked = true;
        numberChecked = true;
        String generatedPassword = PasswordGenerator.generate(upperChecked, lowerChecked,
                symbolChecked, numberChecked, 20);
        Assert.assertTrue(hasNumber(generatedPassword));
        Assert.assertTrue(hasLowercase(generatedPassword));
        Assert.assertTrue(hasUppercase(generatedPassword));
        Assert.assertTrue(hasSymbol(generatedPassword));
    }

    @Test
    public void generateOnlyLower() {
        upperChecked = false;
        lowerChecked = true;
        symbolChecked = false;
        numberChecked = false;
        String generatedPassword = PasswordGenerator.generate(upperChecked, lowerChecked,
                symbolChecked, numberChecked, 20);
        Assert.assertFalse(hasNumber(generatedPassword));
        Assert.assertTrue(hasLowercase(generatedPassword));
        Assert.assertFalse(hasUppercase(generatedPassword));
        Assert.assertFalse(hasSymbol(generatedPassword));
    }

    @Test
    public void generateOnlyUpper() {
        upperChecked = true;
        lowerChecked = false;
        symbolChecked = false;
        numberChecked = false;
        String generatedPassword = PasswordGenerator.generate(upperChecked, lowerChecked,
                symbolChecked, numberChecked, 20);
        Assert.assertFalse(hasNumber(generatedPassword));
        Assert.assertFalse(hasLowercase(generatedPassword));
        Assert.assertTrue(hasUppercase(generatedPassword));
        Assert.assertFalse(hasSymbol(generatedPassword));
    }

    @Test
    public void generateNoNumbers() {
        upperChecked = true;
        lowerChecked = true;
        symbolChecked = true;
        numberChecked = false;
        String generatedPassword = PasswordGenerator.generate(upperChecked, lowerChecked,
                symbolChecked, numberChecked, 20);
        Assert.assertFalse(hasNumber(generatedPassword));
        Assert.assertTrue(hasLowercase(generatedPassword));
        Assert.assertTrue(hasUppercase(generatedPassword));
        Assert.assertTrue(hasSymbol(generatedPassword));
    }

    @Test
    public void generateCorrectLength() {
        upperChecked = true;
        lowerChecked = true;
        symbolChecked = true;
        numberChecked = true;
        // Length = 20
        String generatedPassword = PasswordGenerator.generate(upperChecked, lowerChecked,
                symbolChecked, numberChecked, 20);
        Assert.assertTrue(generatedPassword.length() == 20);
        // Length = 8
        generatedPassword = PasswordGenerator.generate(upperChecked, lowerChecked,
                symbolChecked, numberChecked, 8);
        Assert.assertTrue(generatedPassword.length() == 8);
        // Length = 49
        generatedPassword = PasswordGenerator.generate(upperChecked, lowerChecked,
                symbolChecked, numberChecked, 49);
        Assert.assertTrue(generatedPassword.length() == 49);
    }

    @Test
    public void ignoreInputIfNoChecked() {
        upperChecked = false;
        lowerChecked = false;
        symbolChecked = false;
        numberChecked = false;
        String generatedPassword = PasswordGenerator.generate(upperChecked, lowerChecked,
                symbolChecked, numberChecked, 20);
        Assert.assertEquals(generatedPassword, "");
    }

    @Test
    public void ignoreInputIfLengthInvalid() {
        upperChecked = true;
        lowerChecked = true;
        symbolChecked = true;
        numberChecked = true;
        String generatedPassword = PasswordGenerator.generate(upperChecked, lowerChecked,
                symbolChecked, numberChecked, 0);
        Assert.assertEquals(generatedPassword, "");
    }
}