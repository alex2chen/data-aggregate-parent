package com.kxtx.middleware.aggregate.flow.builder;

import com.kxtx.middleware.aggregate.flow.support.*;
import com.kxtx.middleware.aggregate.config.MergeProperties;
import com.kxtx.middleware.aggregate.core.support.ExtensionLoaders;
import com.kxtx.middleware.aggregate.flow.ItemBinder;

import java.util.Collections;
import java.util.List;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/14.
 */
public class Steps {

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
                itemBinders.forEach(v -> next(v));
            });
            return end();
        }

        public ItemBinderChain end() {
            return chain;
        }

        /**
         * 替换为loadAndSort
         *
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
