package com.turkeycrew;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.regex.Pattern;

public class CustomerUtils {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encodePassword(String plainPassword) {
        return passwordEncoder.encode(plainPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public static String generateToken(Integer customerId) {
        // In a real-world scenario, you'd want to customize the claims (e.g., add user roles, expiration time, etc.)
        return Jwts.builder().setSubject(customerId.toString()).signWith(SignatureAlgorithm.HS256, "secretKey") // Use a secure secret key
                .compact();
    }

    public static void clearTokenFromClient(HttpServletResponse response) {
        Cookie cookie = new Cookie("authToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    // Email validation helper function
    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }
}
