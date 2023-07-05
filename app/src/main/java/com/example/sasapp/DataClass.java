package com.example.sasapp;

public class DataClass {
    private String userName;
    private String userDOB;
    private String userDis;

    public DataClass(String userName, String userDOB, String userDis) {
        this.userName = userName;
        this.userDOB = userDOB;
        this.userDis = userDis;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDOB() {
        return userDOB;
    }

    public void setUserDOB(String userDOB) {
        this.userDOB = userDOB;
    }

    public String getUserDis() {
        return userDis;
    }

    public void setUserDis(String userDis) {
        this.userDis = userDis;
    }


}
