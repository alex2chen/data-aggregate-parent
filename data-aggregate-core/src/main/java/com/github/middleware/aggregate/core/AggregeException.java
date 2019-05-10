package com.github.middleware.aggregate.core;

/**
 * @Author: alex
 * @Description:
 * @Date: created in 2019/2/15.
 */
public class AggregeException extends RuntimeException {
    private String requestId;
    private String errorCode;
    private String errorMessage;

    public AggregeException(String errorMessage) {
        this(errorMessage, null);
    }

    public AggregeException(Throwable cause) {
        this(null, cause);
    }

    public AggregeException(String errorMessage, Throwable cause) {
        super(null, cause);
        this.errorMessage = errorMessage;
    }

    public AggregeException(String errorMessage, String errorCode, String requestId, Throwable cause) {
        this(errorMessage, cause);
        this.errorCode = errorCode;
        this.requestId = requestId;
    }

    public AggregeException(String errorMessage, String errorCode, String requestId) {
        this(errorMessage, errorCode, requestId, null);
    }

    @Override
    public String getMessage() {
        return super.getMessage()
                + (requestId == null ? "" : "\n[requestId]: " + requestId)
                + (errorCode == null ? "" : "\n[errorCode]: " + errorCode);
    }

}
