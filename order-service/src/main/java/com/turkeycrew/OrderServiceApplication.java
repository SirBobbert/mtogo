package com.turkeycrew;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.turkeycrew")
public class OrderServiceApplication {

    public static void main(String[] args) {


        SpringApplication.run(OrderServiceApplication.class, args);

    }
}

