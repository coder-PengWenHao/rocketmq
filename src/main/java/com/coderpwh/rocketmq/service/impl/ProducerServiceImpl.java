package com.coderpwh.rocketmq.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.coderpwh.rocketmq.domain.OrderPaidEvent;
import com.coderpwh.rocketmq.domain.ProductWithPayload;
import com.coderpwh.rocketmq.domain.User;
import com.coderpwh.rocketmq.mq.boot.producer.ExtRocketMQTemplate;
import com.coderpwh.rocketmq.service.ProducerService;
import com.coderpwh.rocketmq.util.Result;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQLocalRequestCallback;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author coderpwh
 * @date 2023/3/6 16:31
 */
@Service
public class ProducerServiceImpl implements ProducerService {


    private static Logger logger = LoggerFactory.getLogger(ProducerServiceImpl.class);


    @Value("${demo.rocketmq.transTopic}")
    private String springTransTopic;

    @Value("${demo.rocketmq.topic}")
    private String springTopic;

    @Value("${demo.rocketmq.topic.user}")
    private String userTopic;

    @Value("${demo.rocketmq.orderTopic}")
    private String orderPaidTopic;

    @Value("${demo.rocketmq.msgExtTopic}")
    private String msgExtTopic;

    @Value("${demo.rocketmq.bytesRequestTopic}")
    private String stringRequestTopic;


    @Value("${demo.rocketmq.bytesRequestTopic}")
    private String bytesRequestTopic;


    @Value("${demo.rocketmq.objectRequestTopic}")
    private String objectRequestTopic;

    @Value("${demo.rocketmq.genericRequestTopic}")
    private String genericRequestTopic;


    @Resource
    private RocketMQTemplate rocketMQTemplate;


    @Resource
    private ExtRocketMQTemplate extRocketMQTemplate;


    @Override
    public Result testProducer() {
//        testSpringTopic();

//        testUserTopic();

//        testUserTopicByWithPayload();

//        testSpringTopicByExtRocketMQTemplate();

//       testSpringTopicByWithpload();

//        asyncSend();

//        convertAndSendByMsgExtTopic();

//        convertAndSendByMsgExtTopicTag1();

//        testBatchMessages();

//        testSendBatchMessageOrderly();

//         testRocketMQTemplateTransaction();

//       testExtRocketMQTemplateTransaction();

//        testStringRequestTopic();


        // TODO ???????????????
//        testBytesRequestTopic();
        //  TODO ???????????????
//        testObjectRequestTopic();

        //  TODO ???????????????
//        testGenericRequestTopic();

//        testStringRequestTopicBySendAndReceive();

//        testObjectRequestTopicBySendAndReceive();

        return Result.ok();
    }




    /**
     * springTopic ??????
     *
     * @return
     */
    public Result testSpringTopic() {
        SendResult sendResult = rocketMQTemplate.syncSend(springTopic, "Hello World!");
        logger.info("??????[testSpringTopic]??? topic:{},?????????:{}", springTopic, JSON.toJSONString(sendResult));
        return Result.ok();
    }


    /***
     * testUserTopic ??????
     * @return
     */
    public Result testUserTopic() {
        User user = new User();
        user.setUserAge(18);
        user.setUserName("coderpwh");
        SendResult sendResult = rocketMQTemplate.syncSend(userTopic, user);
        logger.info("??????[testUserTopic]??? topic:{},?????????:{}", userTopic, JSON.toJSONString(sendResult));
        return Result.ok();
    }


    /***
     *  testUserTopicByWithPayload ??????
     * @return
     */
    public Result testUserTopicByWithPayload() {
        User user = new User();
        user.setUserAge(20);
        user.setUserName("coderpwh");
        Message<User> message = MessageBuilder.withPayload(user).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON_VALUE).build();

        SendResult sendResult = rocketMQTemplate.syncSend(userTopic, message);
        logger.info("??????testUserTopicByWithPayload???,topic???:{},???????????????:{}", userTopic, JSON.toJSONString(sendResult));
        return Result.ok();
    }


    /***
     *  testSpringTopicByExtRocketMQTemplate ??????
     * @return
     */
    public Result testSpringTopicByExtRocketMQTemplate() {

        Message<byte[]> message = MessageBuilder.withPayload("Hello,World 2023!".getBytes()).build();

        SendResult sendResult = extRocketMQTemplate.syncSend(springTopic, message);

        logger.info("testSpringTopicByWithPayload?????????,topic???:{},???????????????:{}", springTopic, JSON.toJSONString(sendResult));

        return Result.ok();
    }


    /***
     *  testSpringTopicByWithpload ??????
     * @return
     */
    public Result testSpringTopicByWithpload() {

        Message<byte[]> message = MessageBuilder.withPayload("Hello,World! I'm from spring message".getBytes()).build();

        SendResult sendResult = rocketMQTemplate.syncSend(springTopic, message);

        logger.info("??????testSpringTopicByWithpload???,topic:{},???????????????:{}", springTopic, JSON.toJSONString(sendResult));

        return Result.ok();
    }


    /**
     * asyncSend ??????
     *
     * @return
     */

    public Result asyncSend() {

        OrderPaidEvent orderPaidEvent = new OrderPaidEvent("T_001", new BigDecimal("88.00"));

        rocketMQTemplate.asyncSend(orderPaidTopic, orderPaidEvent, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                logger.info("asnc onSuccess ???????????????:{}", JSON.toJSONString(sendResult));
            }

            @Override
            public void onException(Throwable throwable) {
                logger.info("async onException Throwable???:{}", throwable);
            }
        });

        return Result.ok();
    }


    /***
     *convertAndSendByMsgExtTopic ??????
     * @return
     */
    public Result convertAndSendByMsgExtTopic() {
        rocketMQTemplate.convertAndSend(msgExtTopic + ":tag0", "I'm from tag0");
        logger.info("syncSend topic:{},tag:{}", msgExtTopic, "tag0");
        return Result.ok();
    }


    /***
     * convertAndSendByMsgExtTopicTag1 ??????
     * @return
     */
    public Result convertAndSendByMsgExtTopicTag1() {

        rocketMQTemplate.convertAndSend(msgExtTopic + ":tag1", "I'm from tag1");
        logger.info("syncSend topic:{},tag:{}", msgExtTopic, "tag1");
        return Result.ok();
    }


    /***
     *  testBatchMessages??????
     * @return
     */
    public Result testBatchMessages() {
        List<Message> msgs = new ArrayList<>();
        String str = "Hello RocketMQ Batch Msg#";

        for (int i = 0; i < 10; i++) {
            str = str + i;
            Message mssage = MessageBuilder.withPayload(str).setHeader(RocketMQHeaders.KEYS, "KEY_" + i).build();
            msgs.add(mssage);
        }
        SendResult sendResult = rocketMQTemplate.syncSend(springTopic, msgs, 60000);
        logger.info("Batch messages send result:{}", JSON.toJSONString(sendResult));
        return Result.ok();
    }


    /***
     * ???????????? testSendBatchMessageOrderly
     * @return
     */
    public Result testSendBatchMessageOrderly() {

        for (int q = 0; q < 4; q++) {
            List<Message> msgs = new ArrayList<>();

            for (int i = 0; i < 10; i++) {
                int msgIndex = q * 10 + i;
                String msg = String.format("Hello RocketMQ Batch Msg#%d to queue: %d", msgIndex, q);

                Message<String> message = MessageBuilder.withPayload(msg).setHeader(RocketMQHeaders.KEYS, "KEY_" + msgIndex).build();
                msgs.add(message);
            }
            SendResult sendResult = rocketMQTemplate.syncSendOrderly(springTopic, msgs, q + "", 60000);
            logger.info("Batch messages orderly to queue:{},???????????????:{}", sendResult.getMessageQueue().getQueueId(), JSON.toJSONString(sendResult));
        }

        return Result.ok();
    }


    /**
     * testRocketMQTemplateTransaction ??????
     *
     * @return
     */
    public Result testRocketMQTemplateTransaction() {
        String[] tags = new String[]{"TagA", "TagB", "TagC", "TagD", "TagE"};

        for (int i = 0; i < 10; i++) {
            try {
                Message msg = MessageBuilder.withPayload("rocketMQTemplate transactional message" + i).setHeader(RocketMQHeaders.TRANSACTION_ID, "KEY_" + i).build();
                SendResult sendResult = rocketMQTemplate.sendMessageInTransaction(springTopic + ":" + tags[i % tags.length], msg, null);
                logger.info("rocketMQTemplate send Transactional msg body:{},sendResult:{}", msg.getPayload(), sendResult.getSendStatus());
                Thread.sleep(10);
            } catch (Exception e) {
                logger.error("???????????????:{}", e.getMessage());
            }
        }
        return Result.ok();
    }

    public Result testExtRocketMQTemplateTransaction() {
        for (int i = 0; i < 10; i++) {
            try {
                Message msg = MessageBuilder.withPayload("extRocketMQTemplate transactional message" + i).setHeader(RocketMQHeaders.TRANSACTION_ID, "KEY_" + i).build();
                SendResult sendResult = extRocketMQTemplate.sendMessageInTransaction(springTransTopic, msg, null);
                logger.info("testExtRocketMQTemplateTransaction ?????????????????? body:{},????????????sendResult:{}", msg.getPayload(), JSON.toJSONString(sendResult.getSendStatus()));

                Thread.sleep(10);
            } catch (Exception e) {
                logger.error("??????testExtRocketMQTemplateTransaction??? ??????????????????,???????????????:{}", e.getMessage());
            }
        }
        return Result.ok();
    }


    /***
     *  testStringRequestTopic ??????
     * @return
     */
    public Result testStringRequestTopic() {

        String replyString = rocketMQTemplate.sendAndReceive(stringRequestTopic, "request string", String.class);

        logger.info("????????????:{},????????????:{}", "request string", replyString);

        return Result.ok();
    }


    /***
     *  testBytesRequestTopic ??????
     * @return
     */
    public Result testBytesRequestTopic() {
        Message<String> message = MessageBuilder.withPayload("request byte[]").build();

//        byte[] replyBytes = rocketMQTemplate.sendAndReceive(bytesRequestTopic, message, byte[].class, 30);
        byte[] replyBytes = rocketMQTemplate.sendAndReceive(bytesRequestTopic, MessageBuilder.withPayload("request byte[]").build(), byte[].class);
        logger.info("????????????:{},???????????????:{}", "request byte[]", new String(replyBytes));

        return Result.ok();
    }


    /***
     *  testObjectRequestTopic ??????
     * @return
     */
    public Result testObjectRequestTopic() {

        User requestUser = new User();
        requestUser.setUserAge(9);
        requestUser.setUserName("requestUserName");

        User replyUser = rocketMQTemplate.sendAndReceive(objectRequestTopic, requestUser, User.class, "order-id");
        logger.info("???????????????:{},???????????????:{}", JSON.toJSONString(requestUser), JSON.toJSONString(replyUser));

        return Result.ok();
    }


    /***
     *   testGenericRequestTopic ??????
     * @return
     */
    public Result testGenericRequestTopic() {
        String content = "request generic";

        ProductWithPayload<String> replyGenericObject = rocketMQTemplate.sendAndReceive(genericRequestTopic, content, new TypeReference<ProductWithPayload<String>>() {
        }.getType(), 60000, 2);

        logger.info("????????????:{},???????????????:{}", content, replyGenericObject);

        return Result.ok();
    }


    /***
     * testStringRequestTopicBySendAndReceive
     * @return
     */
    public Result testStringRequestTopicBySendAndReceive() {

        String content = "request string";

        rocketMQTemplate.sendAndReceive(stringRequestTopic, content, new RocketMQLocalRequestCallback() {
            @Override
            public void onSuccess(Object obj) {
                logger.info("????????????:{},???????????????:{}", JSON.toJSONString(content), JSON.toJSONString(obj));
            }

            @Override
            public void onException(Throwable throwable) {
                logger.error("???????????????:{}", throwable.getMessage());
            }
        });

        return Result.ok();
    }

    public Result testObjectRequestTopicBySendAndReceive() {
        User user = new User();
        user.setUserAge(9);
        user.setUserName("requestUserName");

        rocketMQTemplate.sendAndReceive(objectRequestTopic, user, new RocketMQLocalRequestCallback<User>() {
            @Override
            public void onSuccess(User message) {
                logger.info("???????????????????????????:{}", JSON.toJSONString(message));
            }

            @Override
            public void onException(Throwable throwable) {
                logger.error("???????????????:{}", throwable.getMessage());
            }
        }, 5000);

        return Result.ok();
    }





}
