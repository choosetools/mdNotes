package com.atguigu.mapper;

import com.atguigu.pojo.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName: EmployeeMapper
 * @Package: com.atguigu.mapper
 * @Author cheng
 * @Create 2024/6/10 23:01
 * @Description: TODO
 */
@Repository
public interface EmployeeMapper {
    List<Employee> queryAll();
}
