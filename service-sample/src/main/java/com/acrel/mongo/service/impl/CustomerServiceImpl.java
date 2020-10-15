package com.acrel.mongo.service.impl;

import com.acrel.common.mongo.service.impl.MongoServiceImpl;
import com.acrel.mongo.entity.Customer;
import com.acrel.mongo.service.CustomerService;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl extends MongoServiceImpl<Customer> implements CustomerService {

}
