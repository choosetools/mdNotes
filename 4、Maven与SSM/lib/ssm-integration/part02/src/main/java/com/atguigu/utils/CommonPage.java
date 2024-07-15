package com.atguigu.utils;

import com.github.pagehelper.PageInfo;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: CommonPage
 * @Package: com.atguigu.utils
 * @Author cheng
 * @Create 2024/6/11 14:00
 * @Description: TODO
 */
@Data
public class CommonPage<T> {
    //查询的结果
    private List<T> data;

    //每页大小
    private Integer pageSize;

    //总记录数
    private Long total;

    //当前页码
    private Integer currentPage;

    public static <T> CommonPage<T> restPage(List<T> list) {
        CommonPage<T> result = new CommonPage<>();
        PageInfo<T> pageInfo = new PageInfo<>(list);
        result.setCurrentPage(pageInfo.getPageNum());
        result.setData(list);
        result.setPageSize(pageInfo.getPageSize());
        result.setTotal(pageInfo.getTotal());
        return result;
    }
}
