package com.nexa.hr_management;

import com.nexa.hr_management.model.Product;

import java.math.BigDecimal;

public class LombokTest {
    public static void main(String[] args) {
        Product product = new Product();
        product.setName("Spring Boot Book");
        product.setPrice(new BigDecimal("29.99"));

        System.out.println("Product name: " + product.getName());
        System.out.println("Product price: " + product.getPrice());
        System.out.println("Product details: " + product);
    }
}