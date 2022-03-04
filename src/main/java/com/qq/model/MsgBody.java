package com.qq.model;

public class MsgBody {
    private String method;
    private String submitName;
    private String targetName;
    private String context;
    private User user;

    public MsgBody(String method, String submitName, String targetName, String context) {
        this.method = method;
        this.submitName = submitName;
        this.targetName = targetName;
        this.context = context;
    }

    public MsgBody(String method, String submitName, String password) {
        this.method = method;
        this.submitName = submitName;
        this.user = new User(submitName,password);
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSubmitName() {
        return submitName;
    }

    public void setSubmitName(String submitName) {
        this.submitName = submitName;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
