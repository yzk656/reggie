package com.itheima.reggie.common;

/**
 * @ClassName: BaseContext
 * @Description: 基于ThreadLocal封装的工具类，用于保存和获取当前用户Id
 * @Author: 杨振坤
 * @date: 2023/5/12 22:03
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
