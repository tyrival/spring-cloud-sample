package com.acrel.mongo.controller;

import com.acrel.api.ControllerName;
import com.acrel.common.mongo.controller.MongoController;
import com.acrel.mongo.entity.Customer;
import com.acrel.mongo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: Mongo示例
 * @Author: ZhouChenyu
 */
@RestController
@RequestMapping(ControllerName.SAMPLE_MONGO)
public class CustomerController extends MongoController<Customer> {

    @Autowired
    private CustomerService customerService;

    @Override
    public CustomerService getService() {
        return this.customerService;
    }
}
