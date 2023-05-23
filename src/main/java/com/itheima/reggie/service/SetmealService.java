package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;

import java.util.List;

/**
 * @ClassName: SetmealService
 * @Description: TODO
 * @Author: 杨振坤
 * @date: 2023/5/12 23:58
 */
public interface SetmealService extends IService<Setmeal> {
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);


    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

    /**
     * 获取套餐信息和套餐对应的菜品信息
     * @param id
     * @return
     */
    public SetmealDto getWithDish(Long id);

    /**
     * 修改套餐信息、菜品信息
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto);
}
