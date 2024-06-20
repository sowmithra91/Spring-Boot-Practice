package com.microservices.apigateway.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class OAuth2Controller {
/*
   @GetMapping("/oauth2/authorization/github")
    public String authorizeLogin() {
        return "redirect:https://github.com/login/oauth/authorize?client_id=Ov23lixmse7EOyJBeMtw&redirect_uri=d6896d200b42349ce5635d047d95d12dbb34250e&scope=read:user"; // Redirect to login
    }
*/

    @GetMapping("/user")
    @CrossOrigin(origins = "*")
    public ResponseEntity<OAuth2User> user(@AuthenticationPrincipal OAuth2User principal) {
        //System.out.println(principal.getAttribute("name").toString());
        return ResponseEntity.ok(principal);
    }

    @GetMapping("/")
    public ResponseEntity<Void> redirectToExternalUrl() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .header("Location", "http://localhost:3000")
                .build();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/test")
    public String test() {
        return "CORS is working!";
    }
    // The above code didn't worked

}
