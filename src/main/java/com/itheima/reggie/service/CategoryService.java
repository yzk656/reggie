package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.entity.Category;

/**
 * @ClassName: CategoryService
 * @Description: TODO
 * @Author: 杨振坤
 * @date: 2023/5/12 22:37
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
