package com.kxtx.middleware.aggregate.source;

import com.kxtx.middleware.aggregate.source.bean.MetaHolder;
import com.kxtx.middleware.aggregate.config.MergeProperties;

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
