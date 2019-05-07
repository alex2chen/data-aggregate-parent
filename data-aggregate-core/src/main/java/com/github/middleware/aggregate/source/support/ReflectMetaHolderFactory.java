package com.github.middleware.aggregate.source.support;

import com.github.middleware.aggregate.config.MergeProperties;
import com.github.middleware.aggregate.source.MetaHolderFactory;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.github.middleware.aggregate.annonation.AggregeField;
import com.github.middleware.aggregate.annonation.AggregeProxyArg;
import com.github.middleware.aggregate.core.AggregeException;
import com.github.middleware.aggregate.source.bean.MetaHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/10.
 */
public class ReflectMetaHolderFactory implements MetaHolderFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReflectMetaHolderFactory.class);
    private Cache<String, List<MetaHolder>> clzMetasCache;

    @Override
    public void lasyInit(MergeProperties properties) {
        clzMetasCache = CacheBuilder.newBuilder().maximumSize(properties.getClzMetasCacheSize()).build();
    }

    @Override
    public List<MetaHolder> create(Class source) {
        Preconditions.checkNotNull(clzMetasCache, "MetaHolderFactory初始化有誤!");
        return read(source);
    }

    private List<MetaHolder> read(Class itemClz) {
        Preconditions.checkNotNull(itemClz, "itemClz is required.");
        try {
            String clzName = itemClz.getName();
            return clzMetasCache.get(clzName, () -> {
                List<MetaHolder> result = Lists.newArrayList();
                Map<String, Field> extFields = Maps.newConcurrentMap();
                ReflectionUtils.doWithFields(itemClz, prop -> {
                    AggregeField fieldMeta = prop.getAnnotation(AggregeField.class);
                    if (fieldMeta != null) {
                        MetaHolder property = new MetaHolder(itemClz, prop, fieldMeta);
                        result.add(property);
                    } else {
                        extFields.put(prop.getName(), prop);
                    }
                });

                result.forEach(x -> {
                    addDependFields(x, extFields, x.getProxyMeta().params(), clzName);
                    addDependFields(x, extFields, x.getBatchProxyMeta().list().params(), clzName);
                    addDependFields(x, extFields, x.getBatchProxyMeta().item().params(), clzName);
                });
                return result;
            });
        } catch (ExecutionException e) {
            throw new AggregeException(e);
        }
    }

    private void addDependFields(MetaHolder metaHolder, Map<String, Field> extFields, AggregeProxyArg[] params, String clzName) {
        if (params != null && params.length > 0) {
            for (AggregeProxyArg param : params) {
                if (!Strings.isNullOrEmpty(param.key())) {
                    Field field = extFields.get(param.key());
                    if (field == null) {
                        LOGGER.warn("failed to get field from class: {} by key: {}", clzName, param.key());
                    } else {
                        metaHolder.addDependFields(param.key(), extFields.get(param.key()));
                    }
                }
            }
        }
    }
}
