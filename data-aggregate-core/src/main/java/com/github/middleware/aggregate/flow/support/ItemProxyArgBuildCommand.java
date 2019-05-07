package com.github.middleware.aggregate.flow.support;

import com.github.middleware.aggregate.flow.ItemCommand;
import com.github.middleware.aggregate.flow.context.Invocation;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.github.middleware.aggregate.annonation.AggregeProxyArg;
import com.github.middleware.aggregate.constant.ArgGetMode;
import com.github.middleware.aggregate.core.AggregeException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: alex
 * @Description: 获取远程服务参数信息
 * @Date: created in 2019/1/11.
 */
public class ItemProxyArgBuildCommand extends AbstractItemCommand {
    @Override
    public String getCommandName() {
        return ItemCommand.PROXYARGBUILD;
    }

    @Override
    public Integer getOrder() {
        return 15;
    }

    @Override
    public void handle(Object item, Invocation invocation) {
        if (invocation.getMetaContext().isBatch()) {
            Optional.ofNullable(invocation.getMetaContext().getItemElementMeta().getBatchProxyMeta().item().params()).ifPresent(x -> {
                invocation.getMetaContext().setProxyParams(getParams(x, invocation, item, true));
            });
        } else {
            Optional.ofNullable(invocation.getMetaContext().getItemElementMeta().getProxyArgMetas()).ifPresent(x -> {
                invocation.getMetaContext().setProxyParams(getParams(x, invocation, item, true));
            });
        }
    }

    protected List<Object> getParams(AggregeProxyArg[] args, Invocation invocation, Object item, boolean isItemOfBatch) {
        List<Object> params = Lists.newArrayList();
        for (AggregeProxyArg arg : args) {
            if (Strings.isNullOrEmpty(arg.paramValue())) {
                Preconditions.checkArgument(!(Strings.isNullOrEmpty(arg.key()) && arg.argGetMode() != ArgGetMode.batch), "参数AggregateProxyArg.key为必填项。");
                params.add(getParamValue(invocation, arg, item, isItemOfBatch));
            } else {
                params.add(arg.paramValue());
            }
        }
        return params;
    }

    private Object getParamValue(Invocation invocation, AggregeProxyArg arg, Object item, boolean isItemOfBatch) {
        switch (arg.argGetMode()) {
            case item:
                return getPropertyValue(invocation, arg.key(), item, isItemOfBatch);
            case session:
                return getSessionValue(invocation, arg.key());
            case batch:
                return invocation.getMetaContext().getBatchProxyResult();
            default:
                throw new AggregeException("未知的参数类型ArgGetMode=" + arg.argGetMode());
        }
    }

    private Object getPropertyValue(Invocation invocation, String name, Object item, boolean isItemOfBatch) {
        Map<String, Field> dependFields = invocation.getMetaContext().getItemElementMeta().getDependFields();
        Preconditions.checkNotNull(dependFields, "failed to get dependFields from class: %s", item.getClass().getName());
        Field field = dependFields.get(name);
        Preconditions.checkNotNull(field, "failed to get field[%s] from class: %s", name, item.getClass().getName());
        field.setAccessible(true);
        if (invocation.getMetaContext().isBatch()) {
            if (isItemOfBatch) {
                return invocation.getMetaContext().getFiledValueCache(item.hashCode());
                //return ReflectionUtils.getField(field, item);
            } else {
                List<Object> fields = Lists.newArrayList();
                invocation.getOrgItems().forEach(x -> Optional.ofNullable(ReflectionUtils.getField(field, x)).ifPresent(y -> {
                    /**
                     * 优化：缓存key为item的hash
                     */
                    invocation.getMetaContext().addFiledValueCache(x.hashCode(), y);
                    if (y instanceof List) {
                        ((List) y).forEach(z -> fields.add(z));
                    } else {
                        fields.add(y);
                    }
                }));
                return fields.isEmpty() ? null : fields;
            }
        } else {
            return ReflectionUtils.getField(field, item);
        }
    }

    private Object getSessionValue(Invocation invocation, String key) {
        return invocation.getAggregeContext().getAttachment(key);
    }
}
