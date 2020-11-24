package com.cp470.lanyard;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;

import java.time.LocalDateTime; // import the LocalDateTime class


public class AccountItem {
    /**
     -------------------------------------------------------
     This class stores account info such as:
     - imageResource
        An icon of the account type
     - title
        The title of the account (eg. mylearningspace)
     - userName
        The userName of the account
     - password
        The password if the account
     - userIdMaster
        The unique Firebase Auth ID of the account this
        password belongs too
     - timestamp
        A time stamp from the Firebase server or client
        device
     -------------------------------------------------------
     */

    private int imageResource;// resource int for icon in list view
    private String title;
    private String userName;
    private String password;
    private String userIdMaster;
    //FIXME private String timestamp;

    public AccountItem() {
        //public no-arg constructor needed for Firestore ¯\_(ツ)_/¯
        //See google docs if you want to learn more
    }

    public AccountItem(FirebaseUser userIdMaster, int imageResource, String title, String userName, String password) {
        this.userIdMaster = userIdMaster.getUid();
        this.imageResource = imageResource;
        this.title = title;
        this.userName = userName;
        this.password = password;
        //FIXME this.timestamp = timestamp;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserIdMaster() {
        return userIdMaster;
    }

    public void setUserIdMaster(String userIdMaster) {
        this.userIdMaster = userIdMaster;
    }

//FIXME
//    public String getTimestamp() {
//        return timestamp;
//    }

//FIXME
//    public void setTimestamp(String timestamp) {
//        this.timestamp = timestamp;
//    }

}
