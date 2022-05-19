package cn.stapxs.blog.mapper;

import cn.stapxs.blog.controller.ArticleController;
import cn.stapxs.blog.model.Article;
import cn.stapxs.blog.model.FileInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ArticleMapper {

    @Select("select count(*) from fb_article")
    int getArticleCount();
    @Select("select art_id,user_id,art_link,art_title,art_date,art_sort,art_statue,art_html,art_appendix " +
            "from fb_article " +
            "order by art_date desc")
    List<Article> getArticleSummaryList();
    @Select("select art_id,user_id,art_link,art_title,art_date,art_sort,art_statue,art_html,art_appendix " +
            "from fb_article " +
            "where art_statue = 1 order by art_date desc limit #{num}, 5")
    List<Article> getArticleSummaryListPage(int num);
    @Select("select * from fb_article where art_id = #{art_id}")
    Article getArticleById(String art_id);
    @Select("select art_id from fb_article where art_id = #{artId} and user_id = #{userId}")
    String getArticleByTID(@Param("userId") int user_id, @Param("artId") String art_id);
    @Select("select art_id from fb_article where art_link=#{art_link}")
    String getIDByLink(String link);
    @Select("select * from fb_file order by file_date desc")
    List<FileInfo> getArticleFileList();

    @Insert("insert into " +
            "fb_article(user_id, art_id, art_link, art_title, art_markdown, art_date) " +
            "values(#{userId}, #{artId}, #{artLink}, #{artTitle}, #{artMarkdown}, NOW())")
    void uploadArticle(@Param("userId") int userId, @Param("artId") String artId, @Param("artLink") String artLink, @Param("artTitle") String artTitle, @Param("artMarkdown") String artMarkdown);
    @Insert("insert into " +
            "fb_file(user_id,file_date,file_name,file_size,file_url) " +
            "values(#{user_id}, NOW(), #{file_name}, #{file_size}, #{file_url})")
    void saveArticleImg(FileInfo fileInfo);

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
    @Delete("delete from fb_file where file_name=#{fileName}")
    void deleteArticleImg(String fileName);
}
