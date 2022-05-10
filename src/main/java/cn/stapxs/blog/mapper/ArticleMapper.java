package cn.stapxs.blog.mapper;

import cn.stapxs.blog.controller.ArticleController;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ArticleMapper {

    @Insert("insert into fb_article(user_id, art_id, art_link, art_title, art_markdown, art_date) values(#{userId}, #{artId}, #{artLink}, #{artTitle}, #{artMarkdown}, NOW())")
    void uploadArticle(@Param("userId") int userId, @Param("artId") String artId, @Param("artLink") String artLink, @Param("artTitle") String artTitle, @Param("artMarkdown") String artMarkdown);

    @Update("update fb_article set art_html=#{html} where art_id = #{artId}")
    void updateArticleHtml(@Param("artId") String artId, @Param("html") String html);
    @Update("update fb_article set " +
            "art_date=#{option.art_date}," +
            "art_link=COALESCE(#{option.art_link}, art_link)," +
            "art_sort=#{option.art_sort}," +
            "art_tag=#{option.art_tag}," +
            "art_quote=#{option.art_quote}," +
            "art_statue=#{option.art_statue}" +
            " where art_id = #{artId}")
    void updateArticleOption(@Param("artId") String artId, @Param("option") ArticleController.OptionInfo option);
}
