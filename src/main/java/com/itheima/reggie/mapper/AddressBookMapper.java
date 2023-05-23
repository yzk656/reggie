package com.itheima.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.itheima.reggie.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: AddressBookMapper
 * @Description: TODO
 * @Author: 杨振坤
 * @date: 2023/5/20 16:22
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
