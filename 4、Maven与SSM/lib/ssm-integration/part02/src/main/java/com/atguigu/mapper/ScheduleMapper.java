package com.atguigu.mapper;

import com.atguigu.pojo.Schedule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName: ScheduleMapper
 * @Package: com.atguigu.mapper
 * @Author cheng
 * @Create 2024/6/11 14:15
 * @Description: TODO
 */
@Repository
public interface ScheduleMapper {
    List<Schedule> queryAll();

    void deleteById(@Param("id") Integer id);

    void saveSchedule(@Param("schedule") Schedule schedule);

    void updateById(@Param("schedule") Schedule schedule);
}
