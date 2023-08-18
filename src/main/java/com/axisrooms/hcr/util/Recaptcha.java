package com.axisrooms.hcr.util;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
public class Recaptcha {
    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";
    private final static String RECAPTCHA_SECRET = "6LdT0UoUAAAAAELNYcgLEnaTf1FATZu3a-TbdCs1";
    public static boolean validateReCaptcha(String reCaptchaResponse){
        try {
            RestTemplate restTemplate = new RestTemplate();
            String recaptchaUrl = RECAPTCHA_VERIFY_URL +
                    "?secret=" +
                    RECAPTCHA_SECRET +
                    "&response=" +
                    reCaptchaResponse;
            ResponseEntity<String> response = restTemplate.postForEntity(recaptchaUrl, null, String.class);
            if (response.getBody() != null) {
                JSONObject json = new JSONObject(response.getBody());
                return json.has("success") && json.getBoolean("success");
            }
        }catch (Exception e){
            log.error("Encountered error", "Encountered exception", e);
        }
        return false;
    }

    public static ResponseEntity<String> accessTokenFailed(){
        JSONObject accesTokenFailed = new JSONObject();
        try {
            accesTokenFailed.put("status","error").put("message","access token not valid.");
        } catch (JSONException e) {
            log.error("access token failed response ",e);
        }
        return ResponseEntity.badRequest().body(accesTokenFailed.toString());
//        return accesTokenFailed.toString();
    }
}
