package com.acrel.mongo.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Document(collection = "customer")
public class Customer {

    /**
     * ID
     */
    @Field("id")
    public Integer id;

    /**
     * 姓名
     */
    @Field("name")
    public String name;

}