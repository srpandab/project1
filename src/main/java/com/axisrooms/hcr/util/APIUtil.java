package com.axisrooms.hcr.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class APIUtil {

    @Autowired
    private RestTemplate restTemplate;

    public String sendRequest(String json, String url){
        try{
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(json, httpHeaders);
            return restTemplate.postForObject(url, entity, String.class);
        } catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return null;
    }

    String getResponse(String params, String url){
        try{
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> entity = new HttpEntity<>(null, httpHeaders);
            log.info("URL : " + url + '?' + params);
            ResponseEntity<String> response = restTemplate.exchange(url + '?' + params, HttpMethod.GET, entity, String.class);
            return response.getBody();
        } catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
