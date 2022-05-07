package cn.stapxs.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Version: 1.0
 * @Date: 2022/02/24 下午 04:24
 * @ClassName: back
 * @Author: Stapxs
 * @Description API 返回基础格式
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Back {
    private int code;
    private String msg;
    private String str;
    private Object data;
}
