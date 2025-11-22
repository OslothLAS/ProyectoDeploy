package com.frontMetaMapa.frontMetaMapa.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class DenyIpFilter extends OncePerRequestFilter {

    @Value("${security.access.denied-ips:}")
    private String deniedIps;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();

        List<String> denied = List.of(deniedIps.split(","))
                .stream().map(String::trim).filter(s -> !s.isEmpty()).toList();

        if (denied.contains(ip)) {
            System.out.println("⛔ Bloqueando IP por deny list: " + ip);
            response.sendError(HttpServletResponse.SC_FORBIDDEN); // 403 DIRECTO
            return;
        }

        filterChain.doFilter(request, response);
    }
}
