package com.github.missthee.vo.user;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserInsertVO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private String nickname;
}
