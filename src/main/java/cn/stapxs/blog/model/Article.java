package cn.stapxs.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Version: 1.0
 * @Date: 2022/05/10 下午 04:47
 * @ClassName: Article
 * @Author: Stapxs
 * @Description 文章信息
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article {
    private String art_id;
    private int user_id;
    private String art_link;
    private String art_title;
    private String art_markdown;
    private String art_html;
    private Date art_date;
    private String art_sort;
    private String art_tag;
    private int art_statue;
    private String art_password;
    private String art_quote;
    private String art_appendix;
}
