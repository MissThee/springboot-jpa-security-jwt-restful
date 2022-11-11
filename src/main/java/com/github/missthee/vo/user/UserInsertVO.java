package com.github.missthee.vo.user;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInsertVO {
    @NonNull
    private String username;
    @NonNull
    private String password;
    private String nickname;
}
