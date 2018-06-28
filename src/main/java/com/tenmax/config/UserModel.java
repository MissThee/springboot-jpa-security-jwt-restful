package com.tenmax.config;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
@Data
class UserModel {
    private String username;
    private String password;
    private List<String> roles;
    UserModel(){
        username="123";
        password= new BCryptPasswordEncoder().encode("123");
        System.out.println("password："+password);
        roles=new ArrayList<String>(){{add("ADMIN");add("PROVIDER");}};
    }
}
