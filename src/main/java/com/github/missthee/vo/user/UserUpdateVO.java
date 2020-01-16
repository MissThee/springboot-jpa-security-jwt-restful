package com.github.missthee.vo.user;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotEmpty;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserUpdateVO extends UserInsertVO {
    @NotEmpty
    private Long id;
}
