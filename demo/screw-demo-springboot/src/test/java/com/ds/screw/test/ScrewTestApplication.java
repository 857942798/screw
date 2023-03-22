package com.ds.screw.test;

import com.ds.screw.demo.ScrewApplication;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {ScrewApplication.class})
@AutoConfigureMockMvc
public class ScrewTestApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ScrewTestApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
