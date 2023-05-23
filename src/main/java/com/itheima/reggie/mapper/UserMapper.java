package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: UserMapper
 * @Description: TODO
 * @Author: 杨振坤
 * @date: 2023/5/20 11:22
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
