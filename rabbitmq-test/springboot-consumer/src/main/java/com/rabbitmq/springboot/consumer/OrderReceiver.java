package com.rabbitmq.springboot.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.springboot.entity.Order;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OrderReceiver {

    @RabbitListener(bindings = @QueueBinding(
        value = @Queue(value = "order-queue",durable = "true"),
            exchange = @Exchange(name = "order-exchange",durable = "true",type = "topic"),
            key = "order.*"
        )
    )
    @RabbitHandler
    public void onOrderMessage(@Payload Order order, @Headers Map<String,Object> headers, Channel channel) throws Exception{
        System.out.println("--------------收到消息，开始消费---------------");
        System.out.println("订单id:" + order.getMessage_id());

        Long delivaryTag = (Long) headers.get(AmqpHeaders.DELIVERY_TAG);
        channel.basicAck(delivaryTag,false);
    }
}
