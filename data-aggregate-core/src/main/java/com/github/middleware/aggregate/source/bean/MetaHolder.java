package com.github.middleware.aggregate.source.bean;


import com.google.common.collect.Maps;
import com.github.middleware.aggregate.annonation.AggregeBatchProxy;
import com.github.middleware.aggregate.annonation.AggregeField;
import com.github.middleware.aggregate.annonation.AggregeProxy;
import com.github.middleware.aggregate.annonation.AggregeProxyArg;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/1/10.
 */
public class MetaHolder {
    private Class sourceClz;
    private FieldVisitor sourceField;
    private AggregeField fieldMeta;
    private AggregeProxy proxyMeta;
    private AggregeProxyArg[] proxyArgMetas;
    private Map<String, FieldVisitor> dependFields;
    private AggregeBatchProxy batchProxyMeta;
    private PropertyDescriptor propertyDescriptor;
    private PropertyEditor propertyEditor;

    public MetaHolder(Class sourceClz, Field field, AggregeField fieldMeta) {
        this.sourceClz = sourceClz;
        this.sourceField = new FieldVisitor(field);
        Optional.ofNullable(fieldMeta).ifPresent(x -> {
            this.fieldMeta = fieldMeta;
            if (x.proxy() != null) {
                this.proxyMeta = x.proxy();
                this.proxyArgMetas = x.proxy().params();
            }
            if (x.batchProxy() != null) {
                this.batchProxyMeta = x.batchProxy();
            }
        });
        dependFields = Maps.newHashMap();
    }

    public Class getSourceClz() {
        return sourceClz;
    }

    public void setSourceClz(Class sourceClz) {
        this.sourceClz = sourceClz;
    }

    public FieldVisitor getSourceField() {
        return sourceField;
    }

    public void setSourceField(FieldVisitor sourceField) {
        this.sourceField = sourceField;
    }

    public AggregeField getFieldMeta() {
        return fieldMeta;
    }

    public void setFieldMeta(AggregeField fieldMeta) {
        this.fieldMeta = fieldMeta;
    }

    public AggregeProxy getProxyMeta() {
        return proxyMeta;
    }

    public void setProxyMeta(AggregeProxy proxyMeta) {
        this.proxyMeta = proxyMeta;
    }

    public AggregeProxyArg[] getProxyArgMetas() {
        return proxyArgMetas;
    }

    public void setProxyArgMetas(AggregeProxyArg[] proxyArgMetas) {
        this.proxyArgMetas = proxyArgMetas;
    }

    public Map<String, FieldVisitor> getDependFields() {
        return dependFields;
    }

    public void setDependFields(Map<String, FieldVisitor> dependFields) {
        this.dependFields = dependFields;
    }

    public void addDependFields(String name, Field dependField) {
        this.dependFields.put(name, new FieldVisitor(dependField));
    }

    public AggregeBatchProxy getBatchProxyMeta() {
        return batchProxyMeta;
    }

    public void setBatchProxyMeta(AggregeBatchProxy batchProxyMeta) {
        this.batchProxyMeta = batchProxyMeta;
    }
}
