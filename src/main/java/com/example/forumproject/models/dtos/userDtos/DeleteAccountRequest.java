package com.example.forumproject.models.dtos.userDtos;

import jakarta.validation.constraints.NotBlank;

public class DeleteAccountRequest {

    @NotBlank(message = "Wrong Password")
    private String password;

    @NotBlank(message = "Wrong Capcha")
    private String capcha;

    public DeleteAccountRequest() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCapcha() {
        return capcha;
    }

    public void setCapcha(String capcha) {
        this.capcha = capcha;
    }
}
