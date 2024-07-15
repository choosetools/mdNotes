package com.atguigu.controller;

import com.atguigu.pojo.Schedule;
import com.atguigu.service.ScheduleService;
import com.atguigu.utils.CommonPage;
import com.atguigu.utils.CommonResult;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @ClassName: ScheduleController
 * @Package: com.atguigu.controller
 * @Author cheng
 * @Create 2024/6/11 14:14
 * @Description: TODO
 */
@CrossOrigin
@Controller
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;

    /**
     * 分页查询
     * @param pageSize
     * @param currentPage
     * @return
     */
    @RequestMapping(value = "/schedule/{pageSize}/{currenPage}", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<CommonPage<Schedule>> getSchedule(
            @PathVariable("pageSize") Integer pageSize,
            @PathVariable("currenPage") Integer currentPage){
        PageHelper.startPage(currentPage, pageSize);
        List<Schedule> schedules = scheduleService.queryList();
        return CommonResult.success(CommonPage.restPage(schedules));
    }

    /**
     * 根据id删除
     * @param id
     * @return
     */
    @RequestMapping(value = "/schedule/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public CommonResult deleteSchedule(@PathVariable("id") Integer id){
        scheduleService.deleteById(id);
        return CommonResult.success(null);
    }


    /**
     * 增加日程
     * @param schedule
     * @return
     */
    @RequestMapping(value = "/schedule", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult postSchedule(@RequestBody Schedule schedule){
        scheduleService.saveSchedule(schedule);
        return CommonResult.success(null);
    }

    @RequestMapping(value = "/schedule", method = RequestMethod.PUT)
    @ResponseBody
    public CommonResult putSchedule(@RequestBody Schedule schedule){
        scheduleService.updateSchedule(schedule);
        return CommonResult.success(null);
    }
}
