package com.atguigu.service.impl;

import com.atguigu.mapper.EmployeeMapper;
import com.atguigu.pojo.Employee;
import com.atguigu.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: EmployeeServiceImpl
 * @Package: com.atguigu.service.impl
 * @Author cheng
 * @Create 2024/6/10 22:59
 * @Description: TODO
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public List<Employee> queryAllEmps() {
        return employeeMapper.queryAll();
    }
}
