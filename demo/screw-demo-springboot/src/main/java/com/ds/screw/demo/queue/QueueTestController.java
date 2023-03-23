package com.ds.screw.demo.queue;

import cn.hutool.core.date.DateUtil;
import com.ds.screw.auth.AuthUtils;
import com.ds.screw.queue.QueueUtils;
import com.ds.screw.queue.action.ActionQueueListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *
 * </p>
 *
 * @author dongsheng
 */
@RestController
@RequestMapping("/queue")
public class QueueTestController {

    @RequestMapping("/send")
    public String producer() {
        Map<String, Object> params=new HashMap<>();
        params.put("msg", "这个一个测试消息");
        System.out.println("消息发送时间："+ DateUtil.now());
        QueueUtils.sendMessage("testTopic",
                "testKey",
                params
        );
        // 发送一条延迟处理的消息，1分钟后再处理，单位：秒
        QueueUtils.sendMessage("delayTopic",
                "delayKey",
                params, 60L
        );
        return "success";
    }

    @ActionQueueListener(name = "testTopic", zoneSize = 1, threadSize = 1, pollSize = 1, maxWait = 3000, isDelay=false)
    public void consumer(List<Map<String,Object>> messages) {
        for (Map<String, Object> message : messages) {
            System.out.println("testTopic消息接收时间："+ DateUtil.now()+"-----"+message.get("msg"));
        }
    }

    @ActionQueueListener(name = "delayTopic", zoneSize = 1, threadSize = 1, pollSize = 1, maxWait = 3000, isDelay=true)
    public void consumer2(List<Map<String,Object>> messages) {
        for (Map<String, Object> message : messages) {
            System.out.println("delayTopic消息接收时间："+ DateUtil.now()+"-----"+message.get("msg"));
        }
    }

}
