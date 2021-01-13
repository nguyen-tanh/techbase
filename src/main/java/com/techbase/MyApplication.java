package com.techbase;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author nguyentanh
 */
@SpringBootApplication(
        scanBasePackages = {"com.techbase", "configuration"}
)
public class MyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }

}
