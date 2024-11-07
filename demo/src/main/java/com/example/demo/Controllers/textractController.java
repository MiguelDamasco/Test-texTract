package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.DTO.userDTO;
import com.example.demo.Services.textService;

@RestController
@RequestMapping("/text")
public class textractController {
    
    @Autowired
    private textService myTextService;

    @GetMapping
    public userDTO getText() {
        return this.myTextService.getText();
    }
}
