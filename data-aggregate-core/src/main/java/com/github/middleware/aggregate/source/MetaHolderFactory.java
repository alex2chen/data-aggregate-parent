package com.github.middleware.aggregate.source;

import com.github.middleware.aggregate.config.MergeProperties;
import com.github.middleware.aggregate.source.bean.MetaHolder;

import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/10.
 */
public interface MetaHolderFactory {
    void lasyInit(MergeProperties properties);

    List<MetaHolder> create(Class source);
}
