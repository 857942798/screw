package com.ds.screw.demo;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@ComponentScan(
        basePackages = "com.ds.screw"
)
public class ScrewApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ScrewApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
