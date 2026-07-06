package com.luftborn.luftborn_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class LuftbornBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuftbornBackendApplication.class, args);
    }

}
