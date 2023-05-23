package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @ClassName: EmployeeController
 * @Description: TODO
 * @Author: 杨振坤
 * @date: 2023/5/8 22:29
 */
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     *
     * @param request
     * @param employee
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        /*1. 将页面提交的代码进行md5加密处理*/
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        /*2. 根据页面提交的用户名查询数据库*/
        LambdaQueryWrapper<Employee> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(lqw);

        /*3. 如果没有查询到返回登录失败结果*/
        if (emp == null) {
            return R.error("登录失败");
        }

        /*4. 进行密码比对*/
        if (!emp.getPassword().equals(password)) {
            return R.error("登录失败");
        }

        /*5. 检查员工状态是否可用*/
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用");
        }

        /*登录成功,将员工Id存入到Session中并返回登录成功结果*/
        request.getSession().setAttribute("employee", emp.getId());
        return R.success(emp);
    }

    /**
     * 员工退出功能
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        /*1. 清除Session中保存的当前员工的ID*/
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }


    /**
     * 新增员工
     *
     * @param employee
     * @return
     */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        /*设置初始密码123456，需要进行md5加密处理*/
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());

        /*获取当前用户ID*/
//        Long empId = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("新增员工成功");
    }


    /**
     * 员工分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> pageR(int page, int pageSize, String name) {
        /*log.info("page:{},pageSize:{},name:{}",page,pageSize,name);*/

        /*构造分页构造器*/
        Page pageInfo = new Page(page, pageSize);

        /*构造条件构造器*/
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        /*添加过滤条件*/
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        /*添加排序条件*/
        lambdaQueryWrapper.orderByDesc(Employee::getUpdateTime);

        /*执行查询*/
        employeeService.page(pageInfo, lambdaQueryWrapper);
        return R.success(pageInfo);
    }


    /**
     * 根据id修改员工信息
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        long id=Thread.currentThread().getId();
        log.info("线程ID：{}",id);

        /*获取修改人id*/
//        Long empId = (Long) request.getSession().getAttribute("employee");

//        employee.setUpdateUser(empId);
//        employee.setUpdateTime(LocalDateTime.now());

        employeeService.updateById(employee);

        return R.success("员工信息修改成功");
    }


    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到对应员工信息");
    }
}
