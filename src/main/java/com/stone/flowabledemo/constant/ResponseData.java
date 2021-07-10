package com.stone.flowabledemo.constant;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Administrator
 */
@Data
@Accessors(chain = true)
public class ResponseData<T> {

    private static final int SUCCESS = 200;
    private static final int ERROR = 500;
    private static final int FAILED = -1;

    private Integer code;
    private T data;
    private String message;

    public ResponseData() {
        this.code = SUCCESS;
    }

    public ResponseData<T> ok() {
        this.code = SUCCESS;
        return this;
    }

    public ResponseData<T> failed() {
        this.code = FAILED;
        return this;
    }

    public ResponseData<T> error() {
        this.code = ERROR;
        return this;
    }
}
