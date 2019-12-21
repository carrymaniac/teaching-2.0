package com.gdou.teaching.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.util
 * @ClassName: UserVO
 * @Author: carrymaniac
 * @Description: User类VO
 * @Date: 2019/11/18 5:02 下午
 * @Version:
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserVO {
    private Integer userId;
    private String nickname;
    private String userNumber;
    private String headUrl;
    private String userIdent;
    private String mail; //电子邮箱
    private String phone; //电话号码
    private String college; //学院
    private String series;  //系
    private String major;   //专业
    private String className;//班级
}
