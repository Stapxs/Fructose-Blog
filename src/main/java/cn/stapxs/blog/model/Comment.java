package cn.stapxs.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Version: 1.0
 * @Date: 2022/05/19 下午 02:20
 * @ClassName: Comment
 * @Author: Stapxs
 * @Description 评论结构
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    private String com_id;
    private String art_id;
    private int user_id;
    private String user_name;
    private String user_mail;
    private String user_site;
    private String com_comment_md;
    private String com_comment;
    private String com_children;
    private String com_time;
}
