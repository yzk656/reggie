package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: EmployeeMapper
 * @Description: TODO
 * @Author: 杨振坤
 * @date: 2023/5/8 22:24
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
