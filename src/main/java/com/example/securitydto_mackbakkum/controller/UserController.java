package com.example.securitydto_mackbakkum.controller;

import com.example.securitydto_mackbakkum.dto.LoginResponseDto;
import com.example.securitydto_mackbakkum.dto.UserDto;
import com.example.securitydto_mackbakkum.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/api")
public class UserController {

    private final Map<String, User> userStore = new HashMap<>();
    private final Map<String, String> tokenStore = new HashMap<>();

    @PostMapping("/register")
    public String register(@RequestBody UserDto userDto) {
        String username = userDto.getUsername();
        String password = userDto.getPassword();

        if (userStore.containsKey(username)) {
            return "Username already in use.";
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        User user = new User (username, hashedPassword);
        userStore.put(username, user);

        System.out.println("User registered: " + username); // TEST for debug
        return "Registration success.";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDto userDto) {
        System.out.println("login called");         // TEST for debug
        try {
            String username = userDto.getUsername();
            String password = userDto.getPassword();

            User user = userStore.get(username);
            if (user != null) {
                boolean passwordMatches = BCrypt.checkpw(password, user.getHashedPassword());
                System.out.println("Password matches: " + passwordMatches);

                if (passwordMatches) {
                    String token = UUID.randomUUID().toString();
                    tokenStore.put(token, username);
                    System.out.println("Token generated: " + token);
                    return ResponseEntity.ok(new LoginResponseDto(token));
                } else {
                    System.out.println("Password does not match.");
                }
            } else {
                System.out.println("User not found.");
            }
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during login.");
        }
    }


    @GetMapping("/protected")
    public String protectedEndpoint(@RequestHeader("Authorization") String token) {
        String username = tokenStore.get(token);
        if (username != null) {
            return username + "\nSuccessfully accessed the protected data";
        } else {
            return "Invalid token probably.";
        }
    }
}
