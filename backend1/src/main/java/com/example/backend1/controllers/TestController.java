package com.example.backend1.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend1.model.Product;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private Map<String, Product> productMap;

    @GetMapping("/getProducts")
    public String getProducts() {
        return productMap.values().toString();
    }

    @PostMapping("/addProduct")
    public String addProduct(@RequestParam String id, @RequestParam String name, @RequestParam double price) {
        Product product = new Product(id, name, price);
        productMap.put(id, product);
        //sends async msg to rabbitmq to notify other services about the new product
        rabbitTemplate.convertAndSend("product.added", product);
        return "Product added successfully";
    }
    

}
