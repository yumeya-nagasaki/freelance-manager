package com.example.freelancemanager.client;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class ClientCreateRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    @Email
    @Size(max = 255)
    private String email;

    @Size(max = 1000)
    private String memo;

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    
    public String getMemo() {
        return memo;
    }
}
