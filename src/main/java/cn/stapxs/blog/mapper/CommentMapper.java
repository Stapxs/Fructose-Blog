package cn.stapxs.blog.mapper;

import cn.stapxs.blog.model.Comment;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CommentMapper {

    @Insert("insert into " +
            "fb_comment(com_id, art_id, user_id, user_name, user_mail, user_site, com_comment_md, com_comment, com_children, com_date) " +
            "values(#{com_id}, #{art_id}, #{user_id}, #{user_name}, #{user_mail}, #{user_site}, #{com_comment_md}, #{com_comment}, #{com_children}, NOW())")
    void saveComment(Comment comment);

    @Select("select * from fb_comment where com_id = #{com_id}")
    Comment getCommentById(String com_id);
    @Select("select * from fb_comment where art_id = #{art_id} order by com_date desc")
    List<Comment> getArticleCommentList(String art_id);

    @Update("update fb_comment set com_children = #{com_children} where com_id = #{com_id}")
    void updateCommentChildren(Comment comment);

}
