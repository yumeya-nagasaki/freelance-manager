package com.example.freelancemanager.client;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public class ClientCreateRequest {

    @Schema(description = "取引先名", example = "株式会社ABC")
    @NotBlank
    @Size(max = 100)
    private String name;

    @Schema(description = "メールアドレス", example = "abc@example.com")
    @Email
    @Size(max = 255)
    private String email;

    @Schema(description = "備考", example = "この取引先は、ABC株式会社です。")
    @Size(max = 1000)
    private String memo;

    public ClientCreateRequest() {
        // Jackson用にデフォルトコンストラクタを残す
    }

    public ClientCreateRequest(String name, String email, String memo) {
        this.name = name;
        this.email = email;
        this.memo = memo;
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
}
