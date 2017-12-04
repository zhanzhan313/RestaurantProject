package com.example.myrestaurant.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 站站 on 2017/11/26.
 */

public class SignupLogin implements Parcelable {
    private  String username;
    private String password;
    private String actionToDo;
    private OrderList orderList;

    public SignupLogin(String username, String password, String actionToDo) {
        this.username = username;
        this.password = password;
        this.actionToDo = actionToDo;
    }


    protected SignupLogin(Parcel in) {
//        username = in.readString();
//        password = in.readString();
//        actionToDo = in.readString();
//        orderList=(OrderList) in.readValue();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(username);
//        dest.writeString(password);
//        dest.writeString(actionToDo);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SignupLogin> CREATOR = new Creator<SignupLogin>() {
        @Override
        public SignupLogin createFromParcel(Parcel in) {
            return new SignupLogin(in);
        }

        @Override
        public SignupLogin[] newArray(int size) {
            return new SignupLogin[size];
        }
    };

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getActionToDo() {
        return actionToDo;
    }

    public void setActionToDo(String actionToDo) {
        this.actionToDo = actionToDo;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
