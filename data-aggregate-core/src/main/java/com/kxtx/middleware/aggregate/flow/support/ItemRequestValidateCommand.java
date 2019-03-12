package com.kxtx.middleware.aggregate.flow.support;

import com.google.common.base.Preconditions;
import com.kxtx.middleware.aggregate.context.session.BreakDataBindAggregeEvent;
import com.kxtx.middleware.aggregate.flow.context.Invocation;
import com.kxtx.middleware.aggregate.flow.ItemBinder;
import com.kxtx.middleware.aggregate.source.bean.MetaHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/11.
 */
public class ItemRequestValidateCommand extends AbstractItemCommand {
    private static final Logger LOGGER = LoggerFactory.getLogger(ItemRequestValidateCommand.class);

    @Override
    public void handle(Object item, Invocation invocation) {
        Preconditions.checkNotNull(item, "参数校验失败，item为必填项。");
        Preconditions.checkNotNull(invocation, "参数校验失败，invocation为必填项。");
        // 提前终止执行链
        if (invocation.getEnable() == null || invocation.getMetaContext().getItemElementMeta() == null) {
            LOGGER.warn("item[{}]元数据配置有误.", item);
            invocation.getEventBus().post(new BreakDataBindAggregeEvent(this, invocation));
        }
    }

    @Override
    public String getCommandName() {
        return REQUESTVALIDATE;
    }

    @Override
    public Integer getOrder() {
        return 1;
    }
}
