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


import io.grpc.Grpc; 
import io.grpc.InsecureChannelCredentials;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import com.example.Inference;
import com.example.TestServiceGrpc;
// resolved: add import for TestServiceBlockingStub
import com.example.TestServiceGrpc.TestServiceBlockingStub;
import com.example.Inference.ImageData;
import com.example.Inference.TestResult;



@RestController 
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class ApiController {

    private final ApiRepository repository;

    private final TestClient client;
    private final String target; 
    private final ManagedChannel channel;

    public ApiController(ApiRepository repository) {
        this.repository = repository;

        this.target = "inference-server:50051";
        this.channel = Grpc
            .newChannelBuilder(target, InsecureChannelCredentials.create())
            .build(); // resolved : build() added
        this.client = new TestClient(channel);
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



class TestClient { 
    private final TestServiceBlockingStub blockingStub; 

    public TestClient(Channel channel) {
        blockingStub = TestServiceGrpc.newBlockingStub(channel);
    }

    public String test(ImageJson image) {
        return String.format("Grpc response");
    }
}