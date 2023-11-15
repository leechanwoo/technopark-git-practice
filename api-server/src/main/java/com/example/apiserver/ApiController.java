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
import com.example.apiserver.model.ImageJson;


@RestController 
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class ApiController {

    private final ApiRepository repository;

    public ApiController(ApiRepository repository) {
        this.repository = repository;
    }
    
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/inference")
    public ResultJson helloHandler(@RequestBody ImageJson body) {
        String response = String.format("Image shape: (%d, %d, %d)", 
                                        body.width(), 
                                        body.height(),
                                        body.channel());
        return new ResultJson(response);
    }
}