package com.github.missthee.tool.res;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.io.Serializable;
import java.util.Map;
@Data
@Component
@NoArgsConstructor
public class Res<T> implements Serializable {
    @JsonView(Object.class)//使用@JsonView作为controller输出时，可一直包含这些字段
    private Boolean result;
    @JsonView(Object.class)
    private T data;
    @JsonView(Object.class)
    private String msg;

    private Res(Boolean result, T data, String msg) {
        this.setResult(result);
        this.setData(data == null ? (T) Map.of() : data);
        this.setMsg(StringUtils.hasText(msg) ? msg : "");
    }

    public static <T> Res<T> res(Boolean result, T data, String msg) {
        return new Res<>(result, data, msg);
    }

    public static <T> Res<T> res(Boolean result, T data) {
        return new Res<>(result, data, "");
    }

    public static <T> Res<T> res(Boolean result, String msg) {
        return new Res<>(result, null, msg);
    }

    public static <T> Res<T> res(Boolean result) {
        return new Res<>(result, null, "");
    }

    public static <T> Res<T> success(T data, String msg) {
        return new Res<>(true, data, msg);
    }

    public static <T> Res<T> success(T data) {
        return new Res<>(true, data, "");
    }

    public static Res success(String msg) {
        return new Res<>(true, null, msg);
    }

    public static Res success() {
        return new Res<>(true, null, "");
    }

    public static <T> Res<T> failure(T data, String msg) {
        return new Res<>(false, data, msg);
    }

    public static <T> Res<T> failure(T data) {
        return new Res<>(false, data, "");
    }

    public static Res failure(String msg) {
        return new Res<>(false, null, msg);
    }

    public static Res failure() {
        return new Res<>(false, null, "");
    }

}