package com.microservices.productservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("get")
    public ResponseEntity<String> getProduct() {
        return ResponseEntity.ok("Get Product Success!");
    }
}
