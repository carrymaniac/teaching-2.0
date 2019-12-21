package com.gdou.teaching.Enum;

import lombok.Getter;

/**
 * @author carrymaniac
 * @date Created in 18:36 2019-07-28
 * @description
 **/
@Getter
public enum  ResultEnum implements CodeEnum {
    //通用的状态码
    SUCCESS(0, "请求成功"),
    USER_NO_LOGIN(1,"用户未登陆"),
    SERVER_ERROR(500,"服务器异常，请联系管理员"),
    PARAM_ERROR(403, "参数有误，请检查"),
    PARAM_NULL(403,"参数为空,请检查"),
    BAD_REQUEST(400,"参数格式有误或缺失"),
    USER_PASSWORD_ERROR(10,"密码错误"),
    USER_NOT_EXIST(10,"用户不存在"),
    VERIFYCODE_ERROR(10,"验证码错误"),


    //以下错误只用于后台内部,不返回到前台。
    //用户模块的状态码


    //课程模块状态码
    COURSE_SAVE_ERROR(21,"课程信息保存失败"),
    COURSE_STATUS_ERROR(22,"课程状态异常"),
    COURSE_INVALID_ERROR(23,"注销课程失败"),
    COURSE_RESTORE_ERROR(24,"恢复课程失败"),
    COURSE_NOT_EXIST(25,"该课程不存在"),
    COURSE_DETAIL_NOT_EXIST(26,"课程详情不存在"),


    //实验模块状态码
    EXPERIMENT_SAVE_ERROR(31,"实验保存失败"),
    EXPERIMENT_NOT_EXIST(32,"实验主表不存在"),
    EXPERIMENT_DETAIL_NOT_EXIST(33,"实验详情不存在"),
    EXPERIMENT_INVALID_ERROR(34,"实验主表删除失败"),
    EXPERIMENT_STATUS_ERROR(35,"实验状态异常"),

    //实验提交记录模块状态码
    SUBMIT_RECORD_ERROR(41,"提交实验失败"),

    //实验答案模块状态码
    ANSWER_NOT_EXIST(51,"实验答案不存在"),
    ANSWER_INVALID_ERROR(52,"删除实验答案失败"),

    //文件模块状态码
    FILE_ERROR(61,"文件插入数据库失败"),
    FILE_UPLOAD_FAIL(62,"文件上传失败"),

    //班级模块
    CLASSNAME_ERROR(71,"该班级已注册，请检查确认"),
    CLASS_NOT_EXIST(72,"班级不存在"),
    ;
    private final Integer code;
    private final String msg;
    ResultEnum(Integer code,String msg) {
        this.code =code;
        this.msg = msg;
    }


}
