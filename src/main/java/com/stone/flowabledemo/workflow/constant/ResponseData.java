package com.stone.flowabledemo.workflow.constant;

import lombok.Data;

/**
 * @author Administrator
 */
@Data
public class ResponseData<T> {
    private String code;
    private T data;
    private String message;


}
