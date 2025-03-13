package com.example.currencyconversionservice.controller;

import com.example.currencyconversionservice.model.RequestLog;
import com.example.currencyconversionservice.model.User;
import com.example.currencyconversionservice.respository.RequestLogRepository;
import com.example.currencyconversionservice.respository.UserRepository;
import com.example.currencyconversionservice.service.ApiKeyGenerator;
import com.example.currencyconversionservice.service.CurrencyService;
import com.example.currencyconversionservice.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CurrencyController {

    private final CurrencyService currencyService;
    private final ApiKeyService apiKeyService;
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final RequestLogRepository requestLogRepository;

    @Value("${openexchangerates.api.url}")
    private String exchangeApiUrl;

    @Value("${openexchangerates.api.key}")
    private String appId;

    public CurrencyController(CurrencyService currencyService, ApiKeyService apiKeyService, RestTemplate restTemplate, RestTemplate restTemplate1, UserRepository userRepository, RequestLogRepository requestLogRepository) {
        this.currencyService = currencyService;
        this.apiKeyService = apiKeyService;
        this.restTemplate = restTemplate1;
        this.userRepository = userRepository;
        this.requestLogRepository = requestLogRepository;
    }

    @PostMapping("/register")
    public Map<String, String> registerUser(@RequestParam String name) {
        Optional<User> existingUser = userRepository.findByName(name);
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists.");
        }

        String apiKey = ApiKeyGenerator.generateApiKey();
        User newUser = new User(null, apiKey, name);
        userRepository.save(newUser);

        return Map.of("apiKey", apiKey);
    }
}