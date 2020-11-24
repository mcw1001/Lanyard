package com.cp470.lanyard;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;

import java.time.LocalDateTime; // import the LocalDateTime class

/*
This class stores account info such as title, user name, password etc. in object
 */
public class AccountItem {
    private int imageResource;// resource int for icon in list view
    private String title;
    private String userName;
    private String password;
    private String userIdMaster;
    //private String timestamp;

    public AccountItem() {
        //public no-arg constructor needed for firestore ¯\_(ツ)_/¯
    }

    public AccountItem(FirebaseUser userIdMaster, int imageResource, String title, String userName, String password) {
        this.userIdMaster = userIdMaster.getUid();
        this.imageResource = imageResource;
        this.title = title;
        this.userName = userName;
        this.password = password;
        //this.timestamp = timestamp;
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

//    public String getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(String timestamp) {
//        this.timestamp = timestamp;
//    }

//    @NonNull
//    @Override
//    public String toString() {
//        return "AccountItem{" +
//                "title='" + title + '\'' +
//                ", userIdMaster='" + userIdMaster + '\'' +
//                ", userName='" + userName + '\'' +
//                ", password='" + password + '\'' +
//                ", timestamp=" + timestamp.toString() +
//                '}';
//    }

}
