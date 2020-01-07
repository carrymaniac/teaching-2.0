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
    Forbidden(403,"权限错误"),
    PARAM_ERROR(203, "参数有误，请检查"),
    PARAM_NULL(400,"参数为空,请检查"),
    BAD_REQUEST(400,"参数格式有误或缺失"),
    USER_PASSWORD_ERROR(10,"密码错误"),
    USER_NOT_EXIST(10,"用户不存在"),
    VERIFYCODE_ERROR(10,"验证码错误"),


    //以下错误只用于后台内部,不返回到前台。
    //用户模块的状态码

    //课程模块状态码
    COURSE_SAVE_ERROR(500,"课程信息保存失败"),
    COURSE_STATUS_ERROR(203,"课程状态异常"),
    COURSE_INVALID_ERROR(203,"注销课程失败"),
    COURSE_RESTORE_ERROR(203,"恢复课程失败"),
    COURSE_NOT_EXIST(203,"该课程不存在"),
    COURSE_DETAIL_NOT_EXIST(203,"课程信息不存在"),


    //实验模块状态码
    EXPERIMENT_SAVE_ERROR(500,"实验保存失败"),
    EXPERIMENT_NOT_EXIST(203,"实验不存在，换个姿势试试？"),
    EXPERIMENT_DETAIL_NOT_EXIST(203,"实验信息不存在"),
    EXPERIMENT_INVALID_ERROR(500,"实验主表删除失败"),
    EXPERIMENT_STATUS_ERROR(203,"操作失败,实验状态异常,换个姿势试试？"),

    //实验提交记录模块状态码
    SUBMIT_RECORD_ERROR(500,"提交实验失败"),
    RECORD_NOT_EXIST(203,"实验提交表不存在"),

    //实验答案模块状态码
    ANSWER_NOT_EXIST(203,"该实验答案不存在，检查一下试试？"),
    ANSWER_INVALID_ERROR(500,"删除实验答案失败"),
    ANSWER_SAVE_ERROR(500,"实验答案保存失败"),

    //文件模块状态码
    FILE_ERROR(500,"文件插入数据库失败"),
    FILE_UPLOAD_FAIL(500,"文件上传失败"),

    //班级模块
    CLASSNAME_ERROR(203,"该班级已注册，请检查确认"),
    CLASS_NOT_EXIST(203,"班级不存在，请检查一下您的参数"),

    //成绩模块
    ACHIEVEMENT_NOT_EXIST(203,"查询不到课程成绩，换个姿势再试试？"),
    ;
    private final Integer code;
    private final String msg;
    ResultEnum(Integer code,String msg) {
        this.code =code;
        this.msg = msg;
    }


}
