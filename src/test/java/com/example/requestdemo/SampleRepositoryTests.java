package com.example.requestdemo;

import org.apache.http.impl.bootstrap.HttpServer;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.mockito.internal.util.reflection.ReflectionMemberAccessor;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;

import java.nio.charset.StandardCharsets;
import java.util.function.Supplier;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
// @RunWith(SpringRunner.class)
// @ExtendWith(MockitoExtension.class) 
public class SampleRepositoryTests {

    // @Autowired
    // SampleRepository repository;
    // @Autowired
    // ObjectMapper mapper;

    private ObjectMapper mapper;
    // private RestTemplate restTemplate;
    // private RestTemplateBuilder builder;
    private MockRestServiceServer mockServer;
    private SampleRepository repository;

    @Before
    public void init() {
        this.mapper = new ObjectMapper();
        // resttemplateをmock化
        RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        RestTemplate restTemplate = new RestTemplate();
        doReturn(builder).when(builder).requestFactory(ArgumentMatchers.<Supplier<ClientHttpRequestFactory>>any());
        doReturn(builder).when(builder).messageConverters(ArgumentMatchers.any(HttpMessageConverter.class));
        doReturn(restTemplate).when(builder).build();
        this.mockServer = MockRestServiceServer.bindTo(restTemplate).build();
        this.repository = new SampleRepository(builder);
    }

    @BeforeAll
    public void setup() {
        this.mockServer.reset();
    }


    @Test
    public void test1() throws Exception {

        // resttemplateをmock化
        // RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        // RestTemplate restTemplateMock = new RestTemplate();
        // doReturn(builder).when(builder).requestFactory(ArgumentMatchers.<Supplier<ClientHttpRequestFactory>>any());
        // doReturn(builder).when(builder).messageConverters(ArgumentMatchers.any(HttpMessageConverter.class));
        // doReturn(restTemplateMock).when(builder).build();
        // MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplateMock).build();

        // respnseの設定
        SampleData testData = SampleData.builder().code("OKOK*******").build();
        String responseBody = this.mapper.writeValueAsString(testData);
        mockServer.expect(requestTo("http://localhost:8080/test")).andRespond(withSuccess(responseBody, MediaType.APPLICATION_JSON));

        // 期待値
        String expected = this.mapper.writeValueAsString(testData);

        // 実行
        // SampleRepository repository = new SampleRepository(builder);
        SampleData data = repository.get();
        String actual = this.mapper.writeValueAsString(data);

        System.out.println("*** EXPECTED:" + expected);
        System.out.println("*** ACTUAL:" + actual);

        // 検証
        assertEquals(expected, actual);
        mockServer.verify();
    }

    @Test
    public void test2() throws Exception {

        // resttemplateをmock化
        // RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        // RestTemplate restTemplateMock = new RestTemplate();
        // doReturn(builder).when(builder).requestFactory(ArgumentMatchers.<Supplier<ClientHttpRequestFactory>>any());
        // doReturn(builder).when(builder).messageConverters(ArgumentMatchers.any(HttpMessageConverter.class));
        // doReturn(restTemplateMock).when(builder).build();
        // MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplateMock).build();

        // respnseの設定
        SampleData testData = SampleData.builder().code("BAD REQUEST *******").build();
        String expected = this.mapper.writeValueAsString(testData);
        mockServer.expect(requestTo("http://localhost:8080/test")).andRespond(withBadRequest().contentType(MediaType.APPLICATION_JSON).body(expected));

        // 実行
        String actual = null;
        // SampleRepository repository = new SampleRepository(builder);
        try {
            repository.get();
        } catch (HttpClientErrorException ex)  {
            actual = ex.getResponseBodyAsString(StandardCharsets.UTF_8);
        }

        // 検証
        assertEquals(expected, actual);
        mockServer.verify();

    }

    @Test
    public void test3() throws Exception {

        // resttemplateをmock化
        // RestTemplateBuilder builder = mock(RestTemplateBuilder.class);
        // RestTemplate restTemplateMock = new RestTemplate();
        // doReturn(builder).when(builder).requestFactory(ArgumentMatchers.<Supplier<ClientHttpRequestFactory>>any());
        // doReturn(builder).when(builder).messageConverters(ArgumentMatchers.any(HttpMessageConverter.class));
        // doReturn(restTemplateMock).when(builder).build();
        // MockRestServiceServer mockServer = MockRestServiceServer.bindTo(restTemplateMock).build();

        // 期待値の作成
        SampleData testData = SampleData.builder().code("SERVER ERROR *******").build();
        String expected = this.mapper.writeValueAsString(testData);

        // サーバーアクセスのレスポンス設定
        mockServer.expect(requestTo("http://localhost:8080/test")).andRespond(withServerError().contentType(MediaType.APPLICATION_JSON).body(expected));

        // 実行
        // SampleRepository repository = new SampleRepository(builder);
        String actual = null;
        try {
            repository.get();
        } catch (HttpServerErrorException ex) {
            actual = ex.getResponseBodyAsString(StandardCharsets.UTF_8);
        }

        assertEquals(expected, actual);
        mockServer.verify();

    }
}
