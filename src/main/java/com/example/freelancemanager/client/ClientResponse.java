package com.example.freelancemanager.client;

import java.time.LocalDateTime;

public class ClientResponse {
    
    private Long id;
    private String name;
    private String email;
    private String memo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ClientResponse(Client client) {
        this.id = client.getId();
        this.name = client.getName();
        this.email = client.getEmail();
        this.memo = client.getMemo();
        this.createdAt = client.getCreatedAt();
        this.updatedAt = client.getUpdatedAt();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMemo() {
        return memo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
