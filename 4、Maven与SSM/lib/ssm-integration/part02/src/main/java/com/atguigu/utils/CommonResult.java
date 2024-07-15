package com.atguigu.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: CommonResult
 * @Package: com.atguigu.utils
 * @Author cheng
 * @Create 2024/6/11 13:48
 * @Description: TODO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult<T> {
    private String code;

    private Boolean flag;

    private T data;

    public static <T> CommonResult<T> success(T data){
        return new CommonResult<>("200", true, data);
    }

    public static <T> CommonResult<T> fail(){
        return new CommonResult<>("500", false, null);
    }
}
