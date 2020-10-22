package com.cp470.lanyard;

/*
This class stores account info such as title, user name, passward etc. in object
 */
public class AccountItem {
    private int imageResource;// resource int for icon in list view
    private String title;
    private String userName;
    private String password;

    public AccountItem (int inImageResource, String inTitle, String inUserName, String inPass){
        imageResource=inImageResource;
        title=inTitle;
        userName=inUserName;
        password=inPass;
    }

    public int  getImageResource(){
        return imageResource;
    }

    public String getPassword() {
        return password;
    }

    public String getTitle() {
        return title;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
