package cn.stapxs.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Version: 1.0
 * @Date: 2022/05/09 下午 08:20
 * @ClassName: SortInfo
 * @Author: Stapxs
 * @Description 分类信息格式
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SortInfo {
    private String sort_name;
    private String sort_title;
    private String sort_mark;
}
