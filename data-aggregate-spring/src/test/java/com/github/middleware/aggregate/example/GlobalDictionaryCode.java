package com.github.middleware.aggregate.example;

import com.google.common.base.Strings;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/23.
 */
public class GlobalDictionaryCode {
    private static Table<String, String, String> dictionary = HashBasedTable.create();

    public GlobalDictionaryCode() {
        dictionary.put("orderSource", "1", "淘宝");
        dictionary.put("orderSource", "2", "京东");

        dictionary.put("sex", "1", "男");
        dictionary.put("sex", "2", "女");
    }

    public String getDictValue(String dictSource, Object dictType) {
        if (Strings.isNullOrEmpty(dictSource) || dictType == null) {
            return "未知";
        }
        return dictionary.get(dictSource, dictType.toString());
    }
}
