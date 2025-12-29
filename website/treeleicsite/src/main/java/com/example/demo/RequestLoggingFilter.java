package com.example.demo;

import org.springframework.stereotype.Component;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder; // <--- NOVO IMPORT
import java.nio.charset.StandardCharsets; // <--- NOVO IMPORT

@Component
public class RequestLoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        // 1. Obter IP
        String ipAddress = req.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = req.getRemoteAddr();
        }

        // 2. Obter URL base
        String requestUrl = req.getRequestURI();
        
        // 3. Obter e DESCODIFICAR os parâmetros
        String queryString = req.getQueryString();
        
        String fullUrl = requestUrl;
        if (queryString != null) {
            // Aqui está a magia: transforma "%20" em espaço e "%3A" em ":"
            String decodedQuery = URLDecoder.decode(queryString, StandardCharsets.UTF_8);
            fullUrl += "?" + decodedQuery;
        }

        // 4. Print Limpo
        System.out.println("IP: " + ipAddress + " Request: " + fullUrl);

        chain.doFilter(request, response);
    }
}