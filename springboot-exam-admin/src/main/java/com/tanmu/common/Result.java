package com.tanmu.common;

//{code:200,data:name:kkk,msg:成功}
public class Result<T> {
    private String code;
    private String msg;
    private T date; //任意类型

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getDate() {
        return date;
    }

    public void setDate(T date) {
        this.date = date;
    }

    public Result() {
    }

    public Result(T date) {
        this.date = date;
    }
    public static Result success(){
        Result result = new Result<>();
        result.setCode("200");
        result.setMsg("成功");
        return result;
    }
    public static <T> Result<T> success(T data){
        Result<T> result = new Result(data);
        result.setCode("200");
        result.setMsg("成功");
        return result;
    }
    public static Result error(String code,String msg){
        Result result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
