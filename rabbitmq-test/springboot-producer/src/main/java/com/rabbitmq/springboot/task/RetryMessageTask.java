package com.rabbitmq.springboot.task;

import com.rabbitmq.springboot.constant.Constants;
import com.rabbitmq.springboot.dao.mapper.BrokerMessageLogMapper;
import com.rabbitmq.springboot.dao.po.BrokerMessageLogPO;
import com.rabbitmq.springboot.entity.Order;
import com.rabbitmq.springboot.producer.OrderSender;
import com.rabbitmq.springboot.utils.FastJsonConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <br>
 * 标题: 重发消息定时任务<br>
 * 描述: 重发消息定时任务<br>
 * 时间: 2018/09/07<br>
 *
 * @author zc
 */
@Component
public class RetryMessageTask {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderSender orderSender;
    @Autowired
    private BrokerMessageLogMapper brokerMessageLogMapper;

    /**
     * 启动完成3秒后开始执行，每隔10秒执行一次
     */
    @Scheduled(initialDelay = 3000, fixedDelay = 10000)
    public void retrySend() {
        logger.debug("重发消息定时任务开始");
        // 查询 status = 0 和 timeout 的消息日志
        List<BrokerMessageLogPO> pos = this.brokerMessageLogMapper.listSendFailureAndTimeoutMessage();
        for (BrokerMessageLogPO po : pos) {
            logger.debug("处理消息日志：{}",po);
            if (po.getTryCount() >= Constants.MAX_RETRY_COUNT) {
                // 更新状态为失败
                BrokerMessageLogPO messageLogPO = new BrokerMessageLogPO();
                messageLogPO.setMessage_id(po.getMessage_id());
                messageLogPO.setStatus(Constants.OrderSendStatus.SEND_FAILURE);
                this.brokerMessageLogMapper.changeBrokerMessageLogStatus(messageLogPO);
            } else {
                // 进行重试，重试次数+1
                this.brokerMessageLogMapper.updateRetryCount(po);
                Order reSendOrder = FastJsonConvertUtils.convertJsonToObject(po.getMessage(), Order.class);
                try {
                    this.orderSender.send(reSendOrder);
                } catch (Exception ex) {
                    // 异常处理
                    logger.error("消息发送异常：{}", ex);
                }
            }
        }
        logger.debug("重发消息定时任务结束");
    }
}
