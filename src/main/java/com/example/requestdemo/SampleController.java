package com.example.requestdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
    @GetMapping("/test")
    public SampleData get() {
        SampleData data = SampleData.builder().code("TEST").build();

        return data;
    }
}
