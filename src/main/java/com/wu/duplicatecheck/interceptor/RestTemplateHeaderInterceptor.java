package com.wu.duplicatecheck.interceptor;

import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RestTemplateHeaderInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution) throws IOException {

        // Add custom headers here
        request.getHeaders().add("X-Correlation-ID", "your-correlation-id");
        request.getHeaders().add("X-Source-System", "duplicate-check");

        return execution.execute(request, body);
    }
}