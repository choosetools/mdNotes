package com.atguigu.service;

import com.atguigu.pojo.Employee;

import java.util.List;

/**
 * @ClassName: EmployeeService
 * @Package: com.atguigu.service
 * @Author cheng
 * @Create 2024/6/10 22:59
 * @Description: TODO
 */

public interface EmployeeService {
    public List<Employee> queryAllEmps();
}
