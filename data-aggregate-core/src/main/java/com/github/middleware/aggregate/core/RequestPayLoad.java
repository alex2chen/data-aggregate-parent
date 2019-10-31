package com.github.middleware.aggregate.core;

import com.github.middleware.aggregate.annonation.AggregeEnable;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @Author: alex.chen
 * @Description:
 * @Date: 2019/10/30
 */
public class RequestPayLoad<T> implements Serializable {
    private String eventSource;
    private Object springIOC;
    private T data;
    private LasyLoadData lasyLoadData;
    private Boolean parallel;
    private AggregeEnable enable;

    public RequestPayLoad(String eventSource, Object springIOC, T data, Boolean parallel) {
        this.eventSource = eventSource;
        this.springIOC = springIOC;
        this.data = data;
        this.parallel = parallel;
    }

    public RequestPayLoad(Method intercept, Object springIOC, LasyLoadData lasyLoadData, AggregeEnable enable) {
        if (intercept != null) {
            eventSource = String.format("%s.%s", intercept.getDeclaringClass().getName(), intercept.getName());
        }
        this.springIOC = springIOC;
        this.lasyLoadData = lasyLoadData;
        if (enable != null) {
            parallel = enable.parallel();
        }
    }

    public String getEventSource() {
        return eventSource;
    }

    public void setEventSource(String eventSource) {
        this.eventSource = eventSource;
    }

    public Object getSpringIOC() {
        return springIOC;
    }

    public void setSpringIOC(Object springIOC) {
        this.springIOC = springIOC;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public LasyLoadData getLasyLoadData() {
        return lasyLoadData;
    }

    public void setLasyLoadData(LasyLoadData lasyLoadData) {
        this.lasyLoadData = lasyLoadData;
    }

    public Boolean getParallel() {
        return parallel;
    }

    public void setParallel(Boolean parallel) {
        this.parallel = parallel;
    }
}
