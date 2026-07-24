package com.example.freelancemanager.project;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "契約タイプ", example = "FIXED_PRICE")
public enum ContractType {
    
    FIXED_PRICE,
    MONTHLY,
    HOURLY;
}
