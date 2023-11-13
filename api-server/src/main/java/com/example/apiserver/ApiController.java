package com.example.apiserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Base64;
import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.example.apiserver.model.ResultJson;

@RestController 
@RequestMapping("/api")
public class ApiController {

    private final ApiRepository repository;

    public ApiController(ApiRepository repository) {
        this.repository = repository;
    }
    
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/hello")
    public ResultJson helloHandler(@RequestBody String body) {
        System.out.println("request recieved!");

        return new ResultJson("message from server");
    }
}