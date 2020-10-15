package com.acrel.entity.auth;

import com.acrel.entity.base.Base;
import lombok.Data;

import java.util.List;

@Data
public class Role extends Base {

    private String name;

    private String description;

    private List<User> userList;
}
