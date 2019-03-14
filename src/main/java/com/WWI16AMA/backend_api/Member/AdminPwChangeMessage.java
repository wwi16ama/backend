package com.WWI16AMA.backend_api.Member;

public class AdminPwChangeMessage {

    private String newPassword;

    public AdminPwChangeMessage() {

    }

    public AdminPwChangeMessage(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
