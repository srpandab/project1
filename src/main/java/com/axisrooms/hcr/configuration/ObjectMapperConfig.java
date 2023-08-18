package com.axisrooms.hcr.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.databind.DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper buildObjectMapper() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        return new Jackson2ObjectMapperBuilder()
                .defaultViewInclusion(true)
                .failOnUnknownProperties(false)
                .featuresToEnable(ACCEPT_SINGLE_VALUE_AS_ARRAY)
                .modules(javaTimeModule)
                .build();
    }
}
