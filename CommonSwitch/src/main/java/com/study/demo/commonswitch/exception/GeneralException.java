package com.study.demo.commonswitch.exception;

/**
 * 自定义业务异常
 */
public class GeneralException extends Exception{

    public GeneralException() {
        super();
    }

    public GeneralException(String errorMsg) {
        super(errorMsg);
    }

    public GeneralException(String errorMsg, Throwable throwable) {
        super(errorMsg, throwable);
    }
}
