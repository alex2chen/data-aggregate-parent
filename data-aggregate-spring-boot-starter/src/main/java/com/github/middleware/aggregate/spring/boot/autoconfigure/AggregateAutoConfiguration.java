package com.github.middleware.aggregate.spring.boot.autoconfigure;

import com.github.middleware.aggregate.spring.init.DataAggregeAspect;
import com.github.middleware.aggregate.spring.init.SpringContextHolder;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/2/13.
 */
@Configuration
@ConditionalOnClass({EnableDataAggregate.class, Aspect.class})
@AutoConfigureAfter({AopAutoConfiguration.class})
@EnableConfigurationProperties({AggregateProperties.class})
public class AggregateAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean
    public DataAggregeAspect aggregeAspect(AggregateProperties config) {
        DataAggregeAspect aggregeAspect = new DataAggregeAspect();
        aggregeAspect.setConfig(config);
        return aggregeAspect;
    }

    @Bean
    @ConditionalOnMissingBean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }
}
