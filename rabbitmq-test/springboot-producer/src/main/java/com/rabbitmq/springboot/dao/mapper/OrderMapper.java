package com.rabbitmq.springboot.dao.mapper;

import com.rabbitmq.springboot.entity.Order;
import org.springframework.stereotype.Repository;

/**
 * <br>
 * 标题: 表 t_order Mapper<br>
 * 描述: 表 t_order Mapper<br>
 * 时间: 2018/09/06<br>
 *
 * @author zc
 */
@Repository
public interface OrderMapper {

    /**
     * 新增
     *
     * @param order 订单
     */
    void insert(Order order);

}
