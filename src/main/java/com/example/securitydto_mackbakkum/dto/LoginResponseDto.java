package com.example.securitydto_mackbakkum.dto;
import lombok.*;

// Class to send back the token after successful login and that user will need to access protected endpoint.

@Data
@AllArgsConstructor
public class LoginResponseDto {
    private String token;
}