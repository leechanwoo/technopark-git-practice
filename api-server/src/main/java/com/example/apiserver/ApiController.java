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
import com.example.Inference.ImageData;
import com.example.InferenceServiceGrpc;
import com.example.InferenceServiceGrpc.InferenceServiceBlockingStub;
import com.example.Inference.CategoricalResult;

import com.example.TestServiceGrpc;
import com.example.TestServiceGrpc.TestServiceBlockingStub;
import com.example.Inference.TestResult;



@RestController 
@RequestMapping("/api")
@CrossOrigin(origins="*")
public class ApiController {

    private final ApiRepository repository;

    private final TestClient client;
    private final InferenceClient inference_client;
    private final String target; 
    private final ManagedChannel channel;

    public ApiController(ApiRepository repository) {
        this.repository = repository;

        this.target = "inference-server:50051";
        this.channel = Grpc
            .newChannelBuilder(target, InsecureChannelCredentials.create())
            .build(); // resolved : build() added
        this.client = new TestClient(channel);
        this.inference_client = new InferenceClient(channel);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/inference")
    public ResultJson inferenceHandler(@RequestBody ImageJson body) {
        try {
            List<java.lang.Float> response = inference_client.inference(body);
            return new ResultJson(repository.stringify(response));
        } catch (StatusRuntimeException e) {
            System.out.println("error: " + e);
            return new ResultJson(String.format("Error from gRPC Client: %s", e));
        }
    }
    
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/hello")
    public ResultJson helloHandler(@RequestBody ImageJson body) {
        try {
            String response = client.test(body);
            return new ResultJson(response);
        } catch (StatusRuntimeException e) {
            System.out.println("error: " + e);
            return new ResultJson(String.format("Error from gRPC Client: %s", e));
        }
    }
}

class InferenceClient { 
    private final InferenceServiceBlockingStub blockingStub; 

    public InferenceClient(Channel channel) {
        blockingStub = InferenceServiceGrpc.newBlockingStub(channel);
    }

    public List<java.lang.Float> inference(ImageJson image) throws StatusRuntimeException {
        // Error 예정 
        ImageData request = ImageData
                        .newBuilder()
                        .setImage(image.image())
                        .setWidth(image.width())
                        .setChannel(image.channel())
                        .build();
        CategoricalResult response;
        
        response = blockingStub.inference(request);
        return response.getResultList();
    }
}


class TestClient { 
    private final TestServiceBlockingStub blockingStub; 

    public TestClient(Channel channel) {
        blockingStub = TestServiceGrpc.newBlockingStub(channel);
    }

    public String test(ImageJson image) throws StatusRuntimeException {
        ImageData request = ImageData
                        .newBuilder()
                        .setImage(image.image())
                        .setWidth(image.width())
                        .setChannel(image.channel())
                        .build();
        TestResult response;
        
        response = blockingStub.test(request);
        return response.getResult();
    }
}