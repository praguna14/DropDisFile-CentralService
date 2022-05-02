package com.CS6650.CentralManagementService.service;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {

  private final RestTemplate restTemplate;

  public RestService(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  public String getHealth(int serverPort) {
    String url = "http://localhost:" + serverPort + "/health";
    return this.restTemplate.getForObject(url, String.class);
  }
}
