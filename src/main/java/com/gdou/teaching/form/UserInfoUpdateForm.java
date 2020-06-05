package com.gdou.teaching.form;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @ProjectName: teaching
 * @Package: com.gdou.teaching.form
 * @ClassName: UserInfoUpdateForm
 * @Author: carrymaniac
 * @Description:
 * @Date: 2020/6/5 11:41 下午
 * @Version:
 */
@Data
public class UserInfoUpdateForm {
    private String email;
    private String phone;
    private String headUrl;
}
