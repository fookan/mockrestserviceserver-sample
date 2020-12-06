package com.example.requestdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {
    private SampleRepository repository;

    DataController(SampleRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/data")
    public SampleData get() throws Exception {
        SampleData data = repository.get();
        return data;
    }
}
