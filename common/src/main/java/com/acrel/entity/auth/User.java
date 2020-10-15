package com.acrel.entity.auth;

import com.acrel.entity.base.Base;
import com.acrel.enums.auth.UserTypeEnum;
import com.acrel.enums.base.CommonStateEnum;
import lombok.Data;

@Data
public class User extends Base {

    private String account;

    private String password;

    private String name;

    private String phone;

    private String email;

    private UserTypeEnum userType;

    private String alipayAccount;

    private String wechatAccount;

    private CommonStateEnum state;
}
