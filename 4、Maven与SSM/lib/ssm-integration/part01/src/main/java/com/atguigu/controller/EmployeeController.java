package com.atguigu.controller;

import com.atguigu.pojo.Employee;
import com.atguigu.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @ClassName: EmpController
 * @Package: com.atguigu.controller
 * @Author cheng
 * @Create 2024/6/10 22:57
 * @Description: TODO
 */
@Controller
@Slf4j
@RequestMapping("/emp")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Employee> queryList(){
        List<Employee> employees = employeeService.queryAllEmps();
        log.info(employees.toString());
        return employees;
    }
}
