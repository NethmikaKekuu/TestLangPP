package com.Compiler.Backend.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

@Component
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(httpRequest);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(httpResponse);

        long startTime = System.currentTimeMillis();

        // Log request before processing
        logRequestStart(requestWrapper);

        try {
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logRequestComplete(requestWrapper, responseWrapper, duration);
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequestStart(ContentCachingRequestWrapper request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();

        System.out.println("\n========================================");
        System.out.println("INCOMING REQUEST: " + method + " " + uri + (queryString != null ? "?" + queryString : ""));

        // Log headers for all requests
        System.out.println("Headers:");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            System.out.println("  " + headerName + ": " + request.getHeader(headerName));
        }

        // Log request body for POST/PUT/PATCH
        if ("POST".equals(method) || "PUT".equals(method) || "PATCH".equals(method)) {
            String requestBody = getRequestBody(request);
            if (requestBody != null && !requestBody.isEmpty()) {
                System.out.println("Request Body: " + requestBody);
            }
        }

        System.out.println("----------------------------------------");
    }

    private void logRequestComplete(ContentCachingRequestWrapper request,
                                    ContentCachingResponseWrapper response,
                                    long duration) {
        String method = request.getMethod();
        int status = response.getStatus();

        System.out.println("RESPONSE SENT");
        System.out.println("Status: " + status + " " + getStatusText(status));
        System.out.println("Duration: " + duration + "ms");

        // Log response headers
        System.out.println("Response Headers:");
        for (String headerName : response.getHeaderNames()) {
            System.out.println("  " + headerName + ": " + response.getHeader(headerName));
        }

        // Log response body for all methods
        String responseBody = getResponseBody(response);
        if (responseBody != null && !responseBody.isEmpty()) {
            System.out.println("Response Body: " + responseBody);
        } else {
            System.out.println("Response Body: [empty]");
        }

        System.out.println("========================================\n");
    }

    private String getStatusText(int status) {
        return switch (status) {
            case 200 -> "OK";
            case 201 -> "Created";
            case 204 -> "No Content";
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "";
        };
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            try {
                return new String(content, request.getCharacterEncoding());
            } catch (UnsupportedEncodingException e) {
                return "[Unable to read request body]";
            }
        }
        return null;
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            try {
                return new String(content, response.getCharacterEncoding());
            } catch (UnsupportedEncodingException e) {
                return "[Unable to read response body]";
            }
        }
        return null;
    }
}