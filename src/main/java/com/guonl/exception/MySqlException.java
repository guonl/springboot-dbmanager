package com.guonl.exception;

/**
 * Created by guonl
 * Date 2018/11/22 3:57 PM
 * Description:
 */
public class MySqlException extends RuntimeException {
    private int code;

    public MySqlException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
