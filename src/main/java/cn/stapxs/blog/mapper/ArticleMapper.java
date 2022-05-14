package cn.stapxs.blog.mapper;

import cn.stapxs.blog.controller.ArticleController;
import cn.stapxs.blog.model.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    @Select("select art_id,user_id,art_link,art_title,art_date,art_sort,art_statue,art_html,art_appendix " +
            "from fb_article " +
            "order by art_date desc")
    List<Article> getArticleSummaryList();
    @Select("select * from fb_article where art_id = #{art_id}")
    Article getArticleById(String art_id);
    @Select("select art_id from fb_article where art_id = #{artId} and user_id = #{userId}")
    String getArticleByTID(@Param("userId") int user_id, @Param("artId") String art_id);
    @Select("select art_id from fb_article where art_link=#{art_link}")
    String getIDByLink(String link);

    @Insert("insert into " +
            "fb_article(user_id, art_id, art_link, art_title, art_markdown, art_date) " +
            "values(#{userId}, #{artId}, #{artLink}, #{artTitle}, #{artMarkdown}, NOW())")
    void uploadArticle(@Param("userId") int userId, @Param("artId") String artId, @Param("artLink") String artLink, @Param("artTitle") String artTitle, @Param("artMarkdown") String artMarkdown);

    @Update("update fb_article set " +
            "art_title=#{title}, " +
            "art_link=COALESCE(#{link}, art_link), " +
            "art_markdown=#{content} " +
            "where art_id = #{artId}")
    void updateArticle(@Param("artId") String art_id, @Param("title") String title, @Param("link") String link, @Param("content") String content);
    @Update("update fb_article set " +
            "art_html=#{html} " +
            "where art_id = #{artId}")
    void updateArticleHtml(@Param("artId") String artId, @Param("html") String html);
    @Update("update fb_article set " +
            "art_date=#{option.art_date}," +
            "art_link=COALESCE(#{option.art_link}, art_link)," +
            "art_sort=#{option.art_sort}," +
            "art_tag=#{option.art_tag}," +
            "art_quote=#{option.art_quote}," +
            "art_statue=#{option.art_statue}," +
            "art_appendix=COALESCE(#{option.art_appendix}, art_appendix)" +
            " where art_id = #{artId}")
    void updateArticleOption(@Param("artId") String artId, @Param("option") ArticleController.OptionInfo option);

    @Delete("delete from fb_article where art_id = #{artId}")
    void deleteArticle(String artId);
}
