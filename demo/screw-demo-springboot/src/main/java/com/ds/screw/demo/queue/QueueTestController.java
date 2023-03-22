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
        params.put("msg", "消息发送时间："+ DateUtil.now());
        //此处ma_index_detail表中machine_id+index_id 锁定一行数据，应以此作为分区key，避免多线程更新冲突
        QueueUtils.sendMessage("testTopic",
                "testKey",
                params
        );
        return "success";
    }

    @ActionQueueListener(name = "testTopic", zoneSize = 1, threadSize = 1, pollSize = 50, maxWait = 30000)
    public void consumer(List<Map<String,Object>> messages) {
        for (Map<String, Object> message : messages) {
            System.out.println(message.get("msg"));
        }
    }

    @ActionQueueListener(name = "testTopic2", zoneSize = 1, threadSize = 1, pollSize = 50, maxWait = 30000)
    public void consumer2(List<Map<String,Object>> messages) {
        for (Map<String, Object> message : messages) {
            System.out.println(message.get("msg"));
        }
    }

}
