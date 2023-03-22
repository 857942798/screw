package com.ds.screw.demo.auth;


import com.ds.screw.auth.provider.AuthCheckProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CustomAuthCheckProvider implements AuthCheckProvider {

    /**
     * 返回账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId) {
        List<String> list = new ArrayList<>();
        list.add("meun.add");
        list.add("meun.update");
        list.add("meun.get");
        // 支持通配符校验
        list.add("meun.*");
        return list;
    }

    /**
     * 返回账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId) {
        List<String> list = new ArrayList<>();
        list.add("admin");
        list.add("test1");
        list.add("test2");
        return list;
    }
}
