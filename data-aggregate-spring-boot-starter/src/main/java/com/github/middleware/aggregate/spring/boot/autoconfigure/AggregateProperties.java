package com.github.middleware.aggregate.spring.boot.autoconfigure;

import com.github.middleware.aggregate.config.MergeProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/2/13.
 */
@ConfigurationProperties(prefix = "github.aggregate")
public class AggregateProperties extends MergeProperties {
}
