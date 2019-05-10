package com.github.middleware.aggregate.flow.builder;

import com.github.middleware.aggregate.config.MergeProperties;
import com.github.middleware.aggregate.flow.ItemBinder;
import com.github.middleware.aggregate.flow.support.*;
import com.github.middleware.aggregate.core.support.ExtensionLoaders;

import java.util.Collections;
import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/14.
 */
public class Steps {
    private Steps() {
    }

    public static StepBuilder getBuilder() {
        return new StepBuilder();
    }

    public static class StepBuilder {
        private ItemBinderChain chain;
        private MergeProperties properties;

        public StepBuilder start() {
            chain = new ItemBinderChain();
            return this;
        }

        public StepBuilder config(MergeProperties mergeProperties) {
            this.properties = mergeProperties;
            return this;
        }

        public StepBuilder next(ItemBinder itemBinder) {
            if (properties != null) {
                itemBinder.lasyInit(properties);
            }
            chain.addItemBinder(itemBinder);
            return this;
        }

        public ItemBinderChain loadAndSort() {
            ExtensionLoaders.getExtensionLoader(ItemBinder.class).ifPresent(x -> {
                List<ItemBinder> itemBinders = x.getExtensions();
                Collections.sort(itemBinders, (o1, o2) -> o1.getOrder().compareTo(o2.getOrder()));
                itemBinders.forEach(this::next);
            });
            return end();
        }

        public ItemBinderChain end() {
            return chain;
        }

        /**
         * 替换为loadAndSort
         * @deprecated (废弃)
         * @return
         */
        @Deprecated
        public ItemBinderChain standardBuild() {
            return this.next(new ItemRequestValidateCommand())
                    .next(new ItemProxyArgBuildCommand())
                    .next(new ItemProxyFetchDataCommand())
                    .next(new ItemProxyResponseResolverCommand())
                    .next(new ItemResponseCommand()).end();
        }

        public ItemBinderChain build() {
            return chain;
        }
    }
}
