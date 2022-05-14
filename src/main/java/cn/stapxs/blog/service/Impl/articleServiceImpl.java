package cn.stapxs.blog.service.Impl;

import cn.stapxs.blog.controller.ArticleController;
import cn.stapxs.blog.mapper.ArticleMapper;
import cn.stapxs.blog.model.Article;
import cn.stapxs.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Version: 1.0
 * @Date: 2022/05/10 上午 09:35
 * @ClassName: articleServiceImpl
 * @Author: Stapxs
 * @Description TO DO
 **/
@Service
public class articleServiceImpl implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    /**
     * @Author Stapxs
     * @Description 验证文章所有权
     * @Date 上午 09:59 2022/05/11
     * @Param [user_id, art_id]
     * @return boolean
    **/
    @Override
    public boolean verifyArticle(int user_id, String art_id) {
        Optional<String> id = Optional.ofNullable(articleMapper.getArticleByTID(user_id, art_id));
        return id.isPresent();
    }

    /**
     * @Author Stapxs
     * @Description 保存文章基本信息
     * @Date 上午 10:14 2022/05/10
     * @Param [user_id, title, link, content, status]
    **/
    @Override
    public String uploadArticle(int user_id, String title, String link, String content) {
        // 生成去掉分隔符的 UUID 字符串作为文章 ID
        String uuid = java.util.UUID.randomUUID().toString().replaceAll("-", "");
        // 提交数据库
        articleMapper.uploadArticle(user_id, uuid, link, title, content);
        // 返回
        return uuid;
    }

    /**
     * @Author Stapxs
     * @Description 更新文章基本信息
     * @Date 上午 09:47 2022/05/11
     * @Param [title, link, content]
     * @return void
    **/
    @Override
    public void updateArticle(String art_id, String title, String link, String content) {
        articleMapper.updateArticle(art_id, title, link, content);
    }

    /**
     * @Author Stapxs
     * @Description 删除文章
     * @Date 上午 11:43 2022/05/11
     * @Param [art_id]
     * @return void
    **/
    @Override
    public void deleteArticle(String art_id) {
        articleMapper.deleteArticle(art_id);
    }

    /**
     * @Author Stapxs
     * @Description 保存文章预生成 HTML
     * @Date 下午 01:03 2022/05/10
     * @Param [artId, html]
     * @return void
    **/
    @Override
    public void updateArticleHtml(String artId, String html) {
        articleMapper.updateArticleHtml(artId, html);
    }

    /**
     * @Author Stapxs
     * @Description 更新设置项
     * @Date 下午 01:38 2022/05/10
     * @Param [artId, optionInfo]
    **/
    @Override
    public void updateArticleOption(String artId, ArticleController.OptionInfo optionInfo) {
        articleMapper.updateArticleOption(artId, optionInfo);
    }

    /**
     * @Author Stapxs
     * @Description 使用链接获取 ID
     * @Date 下午 03:12 2022/05/14
     * @Param [link]
     * @return java.lang.String
    **/
    @Override
    public String getIDByLink(String link) {
        return articleMapper.getIDByLink(link);
    }

    /**
     * @Author Stapxs
     * @Description 获取所有文章的摘要信息
     * @Date 下午 04:52 2022/05/10
     * @Param []
     * @return java.util.List<cn.stapxs.blog.model.Article>
    **/
    @Override
    public List<Article> getArticleSummaryList() {
        return articleMapper.getArticleSummaryList();
    }

    /**
     * @Author Stapxs
     * @Description 获取文章详情
     * @Date 上午 09:37 2022/05/11
     * @Param [artId]
     * @return cn.stapxs.blog.model.Article
    **/
    @Override
    public Article getArticle(String artId) {
        return articleMapper.getArticleById(artId);
    }
}
