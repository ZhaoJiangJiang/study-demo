package com.study.demo.commonswitch.service.impl;

import com.study.demo.commonswitch.annotation.ServiceSwitch;
import com.study.demo.commonswitch.constant.ServiceSwitchConstant;
import com.study.demo.commonswitch.entity.Order;
import com.study.demo.commonswitch.result.Result;
import com.study.demo.commonswitch.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Override
    @ServiceSwitch(switchKey = ServiceSwitchConstant.ConfigCode.ORDER_PAY_SWITCH)
    public Result<Order> createOrder() {
        Order order = Order.builder()
                .orderId(System.currentTimeMillis())
                .buyerNickName("Alick")
                .createTime(LocalDateTime.now())
                .build();
        return new Result<>(HttpStatus.OK.value(), "挂号下单成功", order);
    }
}
