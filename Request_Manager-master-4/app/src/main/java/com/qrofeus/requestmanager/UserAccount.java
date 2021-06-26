package com.qrofeus.requestmanager;

public class UserAccount {
    private String user_id;
    private String username;
    private String password;
    private String mailID;
    private String phone_number;

    public UserAccount(String user_id, String username, String password, String mailID, String phone_number) {
        this.user_id = user_id;
        this.username = username;
        this.password = password;
        this.mailID = mailID;
        this.phone_number = phone_number;
    }

    public UserAccount() {
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getMailID() {
        return mailID;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
