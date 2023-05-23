package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: OrderMapper
 * @Description: TODO
 * @Author: 杨振坤
 * @date: 2023/5/21 19:07
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
