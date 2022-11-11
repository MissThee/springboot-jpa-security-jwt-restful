package com.github.missthee.vo.login;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
@Data
@Accessors(chain = true)
@ApiModel("LoginVO")
public class LoginVO {
    @NotNull
    @ApiModelProperty(value = "账号", required = true, example = "user")
    private String username;
    @NotNull
    @ApiModelProperty(value = "密码", required = true, example = "123")
    private String password;
}