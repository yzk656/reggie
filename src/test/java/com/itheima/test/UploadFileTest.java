package com.itheima.test;

import org.junit.jupiter.api.Test;

/**
 * @ClassName: UploadFileTest
 * @Description: TODO
 * @Author: 杨振坤
 * @date: 2023/5/13 17:17
 */
public class UploadFileTest {
    @Test
    public void test1(){
        String fileName="aaaaa.jpg";
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        System.out.println(suffix);
    }

    @Test
    public void test(){

    }
}
