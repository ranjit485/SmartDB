package com.yc.smartdb.controller;

import com.yc.smartdb.service.VannaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class VannaController {

    private final VannaService vannaService;

    public VannaController(VannaService vannaService) {
        this.vannaService = vannaService;
    }

    @PostMapping("/ask-vanna")
    public ResponseEntity<String> askVanna(@RequestBody Map<String, String> body) {
        String question = body.get("question");
        if (question == null) {
            return ResponseEntity.badRequest().body("Question is required.");
        }
        String answer = vannaService.askVanna(question);
        return ResponseEntity.ok(answer);
    }
}
