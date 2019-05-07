package com.github.middleware.aggregate.core;

import java.util.List;
import java.util.Optional;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/9.
 */
public interface ExtensionLoader<T> {
    List<T> getExtensions();

    Optional<T> getExtension();

    Optional<T> getExtension(String regex);

}
