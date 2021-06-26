package com.qrofeus.requestmanager;

public class RequestClass {
    private String request_id;
    private String username;
    private String request_subject;
    private String request_details;
    private String email;
    private String phone;

    public RequestClass(String request_id, String username, String request_subject, String request_details, String email, String phone) {
        this.request_id = request_id;
        this.username = username;
        this.request_subject = request_subject;
        this.request_details = request_details;
        this.email = email;
        this.phone = phone;
    }

    public RequestClass() {
    }

    public String getRequest_id() {
        return request_id;
    }

    public String getUsername() {
        return username;
    }

    public String getRequest_subject() {
        return request_subject;
    }

    public String getRequest_details() {
        return request_details;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
