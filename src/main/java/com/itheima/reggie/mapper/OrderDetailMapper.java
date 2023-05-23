package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: OrderDetailMapper
 * @Description: TODO
 * @Author: 杨振坤
 * @date: 2023/5/21 19:08
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
