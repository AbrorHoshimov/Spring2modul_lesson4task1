package com.example.lesson4task1.controller;

import com.example.lesson4task1.payload.LoginDto;
import com.example.lesson4task1.security.JwtProvider;
import com.example.lesson4task1.service.MyAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    MyAuthService myAuthService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtProvider jwtProvider;

    @PostMapping("/login")
    public ResponseEntity<?> loginToSystem(@RequestBody LoginDto loginDto) {
//        UserDetails userDetails = myAuthService.loadUserByUsername(loginDto.getUserName());
//        boolean matches = passwordEncoder.matches(loginDto.getPassword(), userDetails.getPassword());
//        if (matches){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUserName(),
                    loginDto.getPassword()));
            String token = jwtProvider.generatedToken(loginDto.getUserName());
            return ResponseEntity.ok(token);

        } catch (BadCredentialsException exception) {
            return ResponseEntity.status(401).body("login yoki parol xato");

        }

    }

}
