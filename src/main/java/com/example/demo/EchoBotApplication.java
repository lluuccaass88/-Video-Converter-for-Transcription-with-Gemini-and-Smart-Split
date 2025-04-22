package com.example.demo;

import com.example.demo.service.GeminiService;
import com.example.demo.service.MenagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@RequiredArgsConstructor
public class EchoBotApplication {


	public static void main(String[] args) throws Exception {

    var context = SpringApplication.run(EchoBotApplication.class, args);

    MenagerService menagerService = context.getBean(MenagerService.class);

    menagerService.menagerAplication();

    context.close();

  }

}
