package com.example.todoManager.dto.auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PayloadResponse {
    private String username;
    private String expiryDate;
}
