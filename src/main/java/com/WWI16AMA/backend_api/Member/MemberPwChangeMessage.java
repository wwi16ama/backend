package com.WWI16AMA.backend_api.Member;

public class MemberPwChangeMessage extends AdminPwChangeMessage {

    private String password;

    public MemberPwChangeMessage() {

    }

    public MemberPwChangeMessage(String password, String newPassword) {
        this.password = password;
        this.setNewPassword(newPassword);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
