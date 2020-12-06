package com.example.requestdemo;

import java.net.http.HttpClient;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class SampleClient {
    // private RestTemplateBuilder templateBuilder;
    private RestTemplate restTemplate;

    SampleClient(RestTemplateBuilder restTemplateBuilder) {
        ObjectMapper objectMapper = new ObjectMapper();
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter(objectMapper);

        this.restTemplate = restTemplateBuilder.requestFactory(() -> requestFactory).messageConverters(messageConverter)
                .build();

    }

    protected <R> R request(Class<R> type) {
        String url = "http://localhost:8080/test";
        ResponseEntity<R> response = this.restTemplate.exchange(url, HttpMethod.GET, null, type);

        return (R)response.getBody();
    }

}
