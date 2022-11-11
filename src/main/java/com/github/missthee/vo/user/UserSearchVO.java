package com.github.missthee.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchVO {
    @ApiModelProperty(name = "页号,1开始")
    @Range(min = 1)
    private Integer pageNum;
    @ApiModelProperty(name = "每页条数")
    private Integer pageSize;
    @ApiModelProperty(name = "字符串搜索，<属性名，模糊查询内容>")
    private Map<String, String> searchMap;

}
