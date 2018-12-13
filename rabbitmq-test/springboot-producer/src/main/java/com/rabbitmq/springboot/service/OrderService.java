package com.rabbitmq.springboot.service;

import com.rabbitmq.springboot.constant.Constants;
import com.rabbitmq.springboot.dao.mapper.BrokerMessageLogMapper;
import com.rabbitmq.springboot.dao.mapper.OrderMapper;
import com.rabbitmq.springboot.dao.po.BrokerMessageLogPO;
import com.rabbitmq.springboot.entity.Order;
import com.rabbitmq.springboot.producer.OrderSender;
import com.rabbitmq.springboot.utils.FastJsonConvertUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <br>
 * 标题: 订单服务<br>
 * 描述: 订单服务<br>
 * 时间: 2018/09/07<br>
 *
 * @author zc
 */
@Service
public class OrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;
    @Autowired
    private OrderSender orderSender;

    /**
     * 创建订单
     *
     * @param order 订单
     */
    public void create(Order order) {
        // 当前时间
        Date orderTime = new Date();
        // 业务数据入库
        this.orderMapper.insert(order);
        // 消息日志入库
        BrokerMessageLogPO messageLogPO = new BrokerMessageLogPO();
        messageLogPO.setMessage_id(order.getMessage_id());
        messageLogPO.setMessage(FastJsonConvertUtils.convertObjectToJson(order));
        messageLogPO.setTryCount(0);
        messageLogPO.setStatus(Constants.OrderSendStatus.SENDING);
        messageLogPO.setNextRetry(DateUtils.addMinutes(orderTime, Constants.ORDER_TIMEOUT));
        this.brokerMessageLogMapper.insert(messageLogPO);
        // 发送消息
        this.orderSender.send(order);
    }
}