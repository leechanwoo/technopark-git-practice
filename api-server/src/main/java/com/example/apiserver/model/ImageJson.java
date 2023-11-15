
package com.example.apiserver.model;

public record ImageJson(
    String image, 
    int width,
    int height,
    int channel
){ }