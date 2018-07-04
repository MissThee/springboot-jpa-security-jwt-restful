package com.tenmax.model;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

@Data
public class UserModel {
    private int id;
    private String username;
    private String password;
    private List<String> roles;

    public UserModel() {
        id = 9527;
        username = "a";
//        password= new BCryptPasswordEncoder().encode("123");
        password = "$2a$10$Vd1MPEhru5uwYk3PImofi.O3BBlhnnJHXearDXlymv6QlNxkL5SOe";
        roles = new ArrayList<String>() {{
            add("ADMIN");
            add("PROVIDER");
        }};
    }
}
