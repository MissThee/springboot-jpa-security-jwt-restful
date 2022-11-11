package com.github.missthee.vo.user;

import lombok.*;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateVO extends UserInsertVO {
    @NonNull
    private Long id;
}
