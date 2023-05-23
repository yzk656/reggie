package com.itheima.reggie.common;

/**
 * @ClassName: CustomException
 * @Description: 自定义业务异常类
 * @Author: 杨振坤
 * @date: 2023/5/13 0:17
 */
public class CustomException extends RuntimeException{
    public CustomException(String message) {
        super(message);
    }
}
