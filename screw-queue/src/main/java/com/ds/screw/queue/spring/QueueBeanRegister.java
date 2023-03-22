package com.ds.screw.queue.spring;

import com.ds.screw.queue.action.ActionQueueListenerAnnotationBeanPostProcessor;
import org.springframework.context.annotation.Bean;

/**
 * 注册screw-queue所需要的Bean
 *
 * @author dongsheng
 */
public class QueueBeanRegister {

    /**
     * QueueActionBean 注解处理注册
     */
    @Bean
    public ActionQueueListenerAnnotationBeanPostProcessor actionQueueListenerAnnotationBeanPostProcessor() {
        return new ActionQueueListenerAnnotationBeanPostProcessor();
    }
}
