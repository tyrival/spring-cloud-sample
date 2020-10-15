package com.acrel.entity.base;

import com.acrel.enums.base.BoolStateEnum;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.acrel.enums.base.CommonStateEnum;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: 基础对象类
 * @Author: ZhouChenyu
 */
@Data
public class Base implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    private Integer id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人ID
     */
    private Integer createUserId;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 修改人ID
     */
    private Integer updateUserId;

    /**
     * 删除标记
     */
    private BoolStateEnum delFlag;

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    public Date getCreateTime() {
        return createTime;
    }

    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public JSONObject toJson() {
        return (JSONObject) JSONObject.toJSON(this);
    }
}
