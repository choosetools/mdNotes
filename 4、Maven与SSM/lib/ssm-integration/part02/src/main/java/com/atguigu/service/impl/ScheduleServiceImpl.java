package com.atguigu.service.impl;

import com.atguigu.mapper.ScheduleMapper;
import com.atguigu.pojo.Schedule;
import com.atguigu.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: ScheduleServiceImpl
 * @Package: com.atguigu.service.impl
 * @Author cheng
 * @Create 2024/6/11 14:15
 * @Description: TODO
 */
@Service
public class ScheduleServiceImpl implements ScheduleService {
    @Autowired
    private ScheduleMapper scheduleMapper;

    @Override
    public List<Schedule> queryList() {
        return scheduleMapper.queryAll();
    }

    @Override
    public void deleteById(Integer id) {
        scheduleMapper.deleteById(id);
    }

    /**
     * 保存对象
     * @param schedule
     */
    @Override
    public void saveSchedule(Schedule schedule) {
        scheduleMapper.saveSchedule(schedule);
    }

    /**
     * 修改对象
     * @param schedule
     */
    @Override
    public void updateSchedule(Schedule schedule) {
        scheduleMapper.updateById(schedule);
    }


}
