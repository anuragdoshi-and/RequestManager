package com.qrofeus.requestmanager;

public class CompletedRequest {
    private String username;
    private String email;
    private String phoneNo;
    private String req_id;
    private String req_subject;
    private String req_details;
    private String priority;
    private String message;

    public CompletedRequest(String username, String email, String phoneNo, String req_id, String req_subject, String req_details, String priority, String message) {
        this.username = username;
        this.email = email;
        this.phoneNo = phoneNo;
        this.req_id = req_id;
        this.req_subject = req_subject;
        this.req_details = req_details;
        this.priority = priority;
        this.message = message;
    }

    public CompletedRequest() {
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getReq_id() {
        return req_id;
    }

    public String getReq_subject() {
        return req_subject;
    }

    public String getReq_details() {
        return req_details;
    }

    public String getPriority() {
        return priority;
    }

    public String getMessage() {
        return message;
    }
}
