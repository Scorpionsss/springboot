package com.rabbitmq.springboot.entity;

import java.io.Serializable;

public class Order implements Serializable {

    private static final long serialVersionUID = -412431671823236176L;
    private String id;
    private String name;
    private String message_id;//消息发送的唯一id

    public Order() {

    }

    public Order(String id, String name, String message_id) {
        this.id = id;
        this.name = name;
        this.message_id = message_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", message_id='" + message_id + '\'' +
                '}';
    }
}
