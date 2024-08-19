package com.beyond.teenkiri.common;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class SecurityTest {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "testtest";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}
