package com.cp470.lanyard;

import android.os.Parcel;
import android.os.Parcelable;

/*
This class stores account info such as title, user name, passward etc. in object
 */
public class AccountItem implements Parcelable {
    int imageResource;// resource int for icon in list view
    String title;
    String userName;
    String password;
    String note;

    public AccountItem (int inImageResource, String inTitle, String inUserName, String inPass){
        imageResource=inImageResource;
        title=inTitle;
        userName=inUserName;
        password=inPass;
    }

    protected AccountItem(Parcel in) {
        imageResource = in.readInt();
        title = in.readString();
        userName = in.readString();
        password = in.readString();
        note = in.readString();
    }

    public static final Creator<AccountItem> CREATOR = new Creator<AccountItem>() {
        @Override
        public AccountItem createFromParcel(Parcel in) {
            return new AccountItem(in);
        }

        @Override
        public AccountItem[] newArray(int size) {
            return new AccountItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(imageResource);
        dest.writeString(title);
        dest.writeString(userName);
        dest.writeString(password);
        dest.writeString(note);
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
    public String getNote(){return note;}

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
    public void setNote(String note){this.note=note;}
}
