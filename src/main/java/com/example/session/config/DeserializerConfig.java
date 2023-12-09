package com.example.session.config;

import com.example.session.role.RoleType;
import com.example.session.role.RoleTypeDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeserializerConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        module.addDeserializer(RoleType.class, new RoleTypeDeserializer());
        objectMapper.registerModule(module);

        return objectMapper;
    }
}