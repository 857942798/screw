package com.ds.screw.demo.auth;


import com.ds.screw.auth.AuthUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthCheckController {

    @RequestMapping("/authCheck")
    public String authCheck() {
        return "是否具有权限="+AuthUtils.hasPermission("1","meun.get");
    }

}
