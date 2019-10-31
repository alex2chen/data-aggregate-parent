package com.github.middleware.aggregate.source.bean;

import com.github.middleware.aggregate.flow.ItemBinder;
import com.github.middleware.aggregate.util.UnsafeUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * @Author: alex.chen
 * @Description:
 * @Date: 2019/10/30
 */
public class FieldVisitor {
    private Field source;
    private Long offset;
    private Boolean isFast = true;

    public FieldVisitor(Field source) {
        this.source = source;
        if (source != null) {
            offset = UnsafeUtils.unsafe().objectFieldOffset(source);
        }
        if (Boolean.getBoolean(ItemBinder.ISFAST)) {
            isFast = true;
        }
    }

    public Object getField(Object target) {
        if (isFast) {
            return UnsafeUtils.unsafe().getObject(target, offset);
        }
        source.setAccessible(true);
        return ReflectionUtils.getField(source, target);
    }

    public void setField(Object target, Object value) {
        if (isFast) {
            UnsafeUtils.unsafe().putObject(target, offset, value);
            return;
        }
        source.setAccessible(true);
        ReflectionUtils.setField(source, target, value);
    }

    public Field getSource() {
        return source;
    }

    public void setSource(Field source) {
        this.source = source;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }
}
