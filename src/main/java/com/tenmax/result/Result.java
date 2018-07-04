package com.tenmax.result;

import org.springframework.http.HttpStatus;

import java.util.HashMap;

public class Result {
    private int code = HttpStatus.OK.value();
    private String msg = "成功";
    private Object data;

    public Result(Object data) {
        setData(data);
    }

    public Result() {
        setData(new HashMap());
    }

    public Result(int code, String msg, Object data) {
        setCode(code);
        setMsg(msg);
        setData(data);
    }

    public static Result ok(Object data) {
        return new Result(200, "成功。", data);
    }

    public static Result error400(String msg) {
        return new Result(400, msg, new HashMap());
    }

    public static Result error400() {
        return new Result(400, "服务器不理解请求的语法。", new HashMap());
    }

    public static Result notFound(String msg) {
        return new Result(401, msg, new HashMap());
    }

    public static Result unauthorized() {
        return new Result(401, "用户登录信息无效。", new HashMap());
    }

    public static Result unauthorized(String msg) {
        return new Result(401, msg, new HashMap());
    }

    public static Result unauthenticated() {
        return new Result(403, "用户无权限。", new HashMap());
    }

    public static Result unauthenticated(String msg) {
        return new Result(403, msg, new HashMap());
    }

    public static Result notFound() {
        return new Result(404, "未找到指定页面", new HashMap());
    }

    public static Result error(String msg) {
        return new Result(500, msg, new HashMap());
    }

    public static Result error() {
        return new Result(500, "服务器内部错误", new HashMap());
    }

    public static Result error(int code, String msg) {
        return new Result(code, msg, new HashMap());
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
