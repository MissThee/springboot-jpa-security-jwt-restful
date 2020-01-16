package com.github.missthee.vo.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import java.util.Map;

@Data
public class UserSearchVO {
    @ApiModelProperty(name = "页号,1开始")
    @Range(min = 1)
    private Integer pageNum;
    @ApiModelProperty(name = "每页条数")
    private Integer pageSize;
    @ApiModelProperty(name = "字符串搜索，<属性名，模糊查询内容>")
    private Map<String, String> searchMap;

}
