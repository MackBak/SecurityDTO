package com.example.securitydto_mackbakkum.model;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String username;
    private String hashedPassword;
    private String firstName;
    private String lastName;

    public User (String username, String hashedPassword) {
        this.username = username;
        this.hashedPassword = hashedPassword;
    }

}
