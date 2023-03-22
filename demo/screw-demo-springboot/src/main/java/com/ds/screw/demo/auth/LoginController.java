package com.ds.screw.demo.auth;


import com.ds.screw.auth.AuthUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @RequestMapping("/login")
    public String login() {
        AuthUtils.login("1");
        return "登录凭据，token="+AuthUtils.getTokenValue();
    }

}
