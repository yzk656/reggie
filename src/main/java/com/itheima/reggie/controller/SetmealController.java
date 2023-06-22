package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.dto.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealDishService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: SetmealController
 * @Description: 套餐管理
 * @Author: 杨振坤
 * @date: 2023/5/18 7:56
 */
@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;


    /**
     * 新增套餐
     *
     * @param setmealDto
     * @return
     */
    @PostMapping
    //这里精准删除无法实现
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> save(@RequestBody SetmealDto setmealDto) {
        System.out.println(111);
        log.info(setmealDto.toString());

        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功");
    }

    /**
     * 套餐分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        /*分页构造器对象*/
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        /*创建条件构造器*/
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        /*添加查询条件*/
        lambdaQueryWrapper.like(name != null, Setmeal::getName, name);
        /*添加排序条件*/
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        setmealService.page(pageInfo, lambdaQueryWrapper);

        /*对象copy*/
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<Setmeal> records = pageInfo.getRecords();

        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            /*讲菜品信息复制给新对象*/
            BeanUtils.copyProperties(item, setmealDto);//对象拷贝
            /*分类ID*/
            Long categoryId = item.getCategoryId();
            /*分类对象*/
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                /*分类名称*/
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        /*讲数据*/
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    //这里精准删除无法实现
    @CacheEvict(value = "setmealCache",allEntries = true)
    public R<String> delete(@RequestParam List<Long> ids) {
        log.info("ids:{}", ids);
        setmealService.removeWithDish(ids);
        return R.success("删除套餐成功");
    }

    /**
     * 套餐起售、停售功能
     *
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> update(@PathVariable Integer status, @RequestParam List<Long> ids) {
        /*遍历ids*/
        ids.stream().forEach((item) -> {
            /*构建条件构造器*/
            LambdaUpdateWrapper<Setmeal> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            /*添加过滤条件*/
            lambdaUpdateWrapper.in(item != null, Setmeal::getId, item);
            /*设置更新字段内容*/
            lambdaUpdateWrapper.set(Setmeal::getStatus, status);

            /*执行更新方法*/
            setmealService.update(lambdaUpdateWrapper);
        });
        return R.success("更新套餐数据成功");
    }


    /**
     * 获取套餐信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Setmeal> get(@PathVariable Long id) {
        //因为需要查询添加的菜品信息，因此只查询套餐信息是不行的
        SetmealDto setmealDto = setmealService.getWithDish(id);

        return R.success(setmealDto);
    }


    /**
     * 更新套餐信息
     *
     * @param setmealDto
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        //多表操作，执行自建方法
        setmealService.updateWithDish(setmealDto);

        return R.success("修改成功");
    }

    /**
     * 展示套餐信息
     *
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "setmealCache",key = "#setmeal.categoryId + '_' + #setmeal.status")
    public R<List<Setmeal>> get(Setmeal setmeal) {
        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        lambdaQueryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        lambdaQueryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(lambdaQueryWrapper);

        return R.success(list);
    }
}
