package com.study.demo.commonswitch.service;

import com.study.demo.commonswitch.entity.Order;
import com.study.demo.commonswitch.result.Result;

public interface OrderService {

    Result<Order> createOrder();
}
