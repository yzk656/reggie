package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: DishMapper
 * @Description: TODO
 * @Author: 杨振坤
 * @date: 2023/5/12 23:56
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
