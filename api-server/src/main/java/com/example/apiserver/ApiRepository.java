
package com.example.apiserver; 

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDateTime;

import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;


@Repository
public class ApiRepository { 
    public ApiRepository() { }

    public String stringify(List<java.lang.Float> numbers) {
        return numbers.stream()
                      .map(n -> String.valueOf(n))
                      .collect(Collectors.joining(", ", "[", "]"));
    }
    
}