package com.example.forumproject.models.dtos.userDtos;

import jakarta.validation.constraints.NotBlank;

public class DeleteAccountRequest {

    @NotBlank(message = "Wrong Password")
    private String password;

    @NotBlank(message = "Wrong Captcha")
    private String captcha;

    public DeleteAccountRequest() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
