package com.rabbitmq.springboot;

import com.rabbitmq.springboot.entity.Order;
import com.rabbitmq.springboot.producer.OrderSender;
import com.rabbitmq.springboot.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

	@Autowired
	private OrderService orderService;

	@Test
	public void testCreateOrder(){
		Order order = new Order();
		order.setId(String.valueOf(System.currentTimeMillis()));
		order.setName("测试创建订单");
		order.setMessage_id(System.currentTimeMillis() + "$" + UUID.randomUUID().toString().replaceAll("-",""));
		this.orderService.create(order);
	}
}

