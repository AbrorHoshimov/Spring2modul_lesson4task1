package com.example.lesson4task1.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class JwtProvider {

    static String kalitSoz = "AbaHoshimov";
    static long expirationDate = 36000000;

    public static String generatedToken(String username) {
        String token = Jwts
                .builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationDate))
                .signWith(SignatureAlgorithm.HS512, kalitSoz)
                .compact();
        return token;
    }

    public boolean validateToken(String token){
        try {
            Jwts
                    .parser()
                    .setSigningKey(kalitSoz)
                    .parseClaimsJws(token);
            return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public String getUSerFromToken(String token){
        String subject = Jwts
                .parser()
                .setSigningKey(kalitSoz)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return subject;
    }

    public static void main(String[] args) {
        String abror = generatedToken("Abror");
        System.out.println(abror);
    }
}
