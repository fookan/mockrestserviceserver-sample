package com.example.requestdemo;

import java.nio.charset.StandardCharsets;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;

@Repository
public class SampleRepository extends SampleClient {

    public SampleRepository(RestTemplateBuilder restTemplateBuilder) {
        super(restTemplateBuilder);
    }

    public SampleData get() throws RestClientException {
        SampleData data;
        try {
            data = this.request(SampleData.class);
        } catch (HttpClientErrorException ex) {
            // 400系エラー
            System.out.println("400系エラー：HttpClientErrorException");
            System.out.println(ex);
            System.out.println("status code=" + ex.getStatusCode());
            System.out.println("response body=" + ex.getResponseBodyAsString(StandardCharsets.UTF_8));
            throw ex;
        } catch (HttpServerErrorException ex) {
            // 500系エラー
            System.out.println("500系エラー：HttpServerErrorException");
            System.out.println(ex);
            System.out.println("status code=" + ex.getStatusCode());
            System.out.println("response body=" + ex.getResponseBodyAsString(StandardCharsets.UTF_8));
            throw ex;
        } catch(ResourceAccessException ex){
            // アクセスできなかった
            System.out.println("アクセスできなかった：ResourceAccessException");
            System.out.println(ex);
            throw ex;
        }

        return data;
    }
    
}
