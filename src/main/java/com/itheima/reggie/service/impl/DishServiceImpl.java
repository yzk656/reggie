package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.dto.DishDto;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.mapper.DishMapper;
import com.itheima.reggie.service.DishFlavorService;
import com.itheima.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: DishServiceImpl
 * @Description: TODO
 * @Author: 杨振坤
 * @date: 2023/5/12 23:59
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时保存对应的口味数据
     *
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        /*保存菜品的基本信息到菜品表dish*/
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品ID

        /*菜品口味*/
        List<DishFlavor> dishFlavors = dishDto.getFlavors();
        dishFlavors = dishFlavors.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        /*保存菜品口味数据到菜品口味表dish_flavor*/
//        dishFlavorService.saveBatch(dishDto.getFlavors());
        dishFlavorService.saveBatch(dishFlavors);
    }


    /**
     * 根据id查询菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        /*查询菜品信息，从dish表中查询*/
        Dish dish = this.getById(id);

        DishDto dishDto = new DishDto();
        /*对象拷贝*/
        BeanUtils.copyProperties(dish, dishDto);

        /*查询当前菜品信息对应的口味信息，从dish_flavor表中查询*/
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        /*添加条件*/
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> flavors = dishFlavorService.list(queryWrapper);

        dishDto.setFlavors(flavors);

        return dishDto;
    }


    /**
     * 更新菜品信息和菜品口味表
     *
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        /*更新dish表基本信息*/
        this.updateById(dishDto);

        /*清理当前菜品对应的口味数据 ---dish_flavor表中的delete操作*/
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());

        dishFlavorService.remove(queryWrapper);

        /*添加当前提交过来的口味数据  ---dish_flavor表中的insert操作*/
        List<DishFlavor> dishFlavors = dishDto.getFlavors();

        dishFlavors = dishFlavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(dishFlavors);
    }
}
