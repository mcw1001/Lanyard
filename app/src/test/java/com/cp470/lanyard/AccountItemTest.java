package com.cp470.lanyard;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AccountItemTest {
    FirebaseUser userIdMaster = Mockito.mock(FirebaseUser.class);
    int imageResource = 6;
    String title = "Website.com";
    String userName = "Username123";
    String password = "password1234";
    Timestamp timestamp = new Timestamp(new Date(1000000));
    Timestamp expiration = new Timestamp(new Date(2000000));
    int priority = 23;

    public AccountItem createAccountItem() {
        AccountItem item = new AccountItem(userIdMaster, imageResource, title, userName, password,
                timestamp, expiration, priority);
        return item;
    }

    @Test
    public void hasEmptyConstructor() {
        // Empty constructor is required for Firebase
        AccountItem account = new AccountItem();
        assertNotNull(account);
    }

    @Test
    public void canGetValues() {
        AccountItem account = createAccountItem();
        assertEquals(account.getUserIdMaster(), userIdMaster.getUid());
        assertEquals(account.getImageResource(), imageResource);
        assertEquals(account.getTitle(), title);
        assertEquals(account.getUserName(), userName);
        assertEquals(account.getPassword(), password);
        assertEquals(account.getTimestamp(), timestamp);
        assertEquals(account.getExpirationDate(), expiration);
        assertEquals(account.getPriority(), priority);
    }

}