package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.CustomException;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import com.itheima.reggie.mapper.SetmealMapper;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: SetmealServiceImpl
 * @Description: TODO
 * @Author: 杨振坤
 * @date: 2023/5/13 0:00
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联关系
     *
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        log.info(setmealDto.toString());
        /*保存套餐基本信息，操作setmeal，执行insert操作*/
        this.save(setmealDto);

        /*获取菜品信息*/
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        /*保存套餐和菜品的关联信息，操作setmeal_dish,执行insert操作*/
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * 删除套餐，同时删除套餐和菜品的关联数据
     *
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId, ids);
        lambdaQueryWrapper.eq(Setmeal::getStatus, 1);

        int count = this.count(lambdaQueryWrapper);
        if (count > 0) {
            //如何不能删除，跑出一个业务异常
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        //如果可以删除，先删除套餐表中的数据
        this.removeByIds(ids);

        //添加过滤条件
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.in(SetmealDish::getSetmealId, ids);
        /*删除关系表中的数据*/
        setmealDishService.remove(lambdaQueryWrapper1);
    }

    /**
     * 获取套餐信息和套餐对应的菜品信息
     *
     * @param id
     * @return
     */
    public SetmealDto getWithDish(Long id) {
        //获取套餐信息
        Setmeal setmeal = this.getById(id);
        /*创建套餐_菜品关系表信息存放对象*/
        SetmealDto setmealDto = new SetmealDto();

        /*进行拷贝，讲套餐上的数据传送*/
        BeanUtils.copyProperties(setmeal, setmealDto);

        /*查询菜品关系表数据*/
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        /*执行查询方法*/
        List<SetmealDish> list = setmealDishService.list(lambdaQueryWrapper);

        /*将菜品信息存放在套餐_菜品的存储对象中*/
        setmealDto.setSetmealDishes(list);
        return setmealDto;
    }

    /**
     * 修改套餐信息、菜品信息
     *
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto) {
        //更新套餐信息
        this.updateById(setmealDto);

        /*清理当前套餐对应的菜品信息*/
        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());

        //执行删除方法
        setmealDishService.remove(lambdaQueryWrapper);

        //添加前端提交过来的菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();

        /*将套餐ID添加到套餐_菜品表中*/
        setmealDishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }
}
