package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: CategoryController
 * @Description: 分类管理
 * @Author: 杨振坤
 * @date: 2023/5/12 22:35
 */
@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("添加分类成功");
    }


    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        /*分页构造器*/
        Page<Category> pageInfo = new Page<>(page, pageSize);
        /*条件构造器*/
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();
        /*添加排序条件，根据sort进行排序*/
        lambdaQueryWrapper.orderByAsc(Category::getSort);

        /*分页查询*/
        categoryService.page(pageInfo, lambdaQueryWrapper);

        return R.success(pageInfo);
    }


    /**
     * 根据id删除分类
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long ids) {
//        categoryService.removeById(ids);
        categoryService.remove(ids);
        return R.success("分类信息删除成功");
    }


    /**
     * 根据id 修改分类信息
     * @param category
     * @return
     */
    @PutMapping
    public R<String> updte(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category){
        /*条件构造器*/
        LambdaQueryWrapper<Category> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        /*添加条件*/
        lambdaQueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        /*添加排序条件*/
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(lambdaQueryWrapper);

        return R.success(list);
    }
}
