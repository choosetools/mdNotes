package com.atguigu.service;

import com.atguigu.pojo.Schedule;

import java.util.List;

/**
 * @ClassName: ScheduleService
 * @Package: com.atguigu.service
 * @Author cheng
 * @Create 2024/6/11 14:14
 * @Description: TODO
 */
public interface ScheduleService {
    List<Schedule> queryList();

    void deleteById(Integer id);

    void saveSchedule(Schedule schedule);

    void updateSchedule(Schedule schedule);
}
