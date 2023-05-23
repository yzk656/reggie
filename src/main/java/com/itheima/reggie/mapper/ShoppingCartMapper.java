package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: ShoppingCartMapper
 * @Description: TODO
 * @Author: 杨振坤
 * @date: 2023/5/20 23:47
 */
@Mapper
public interface ShoppingCartMapper extends BaseMapper<ShoppingCart> {
}
