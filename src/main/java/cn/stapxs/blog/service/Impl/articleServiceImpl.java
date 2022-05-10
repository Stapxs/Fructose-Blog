package cn.stapxs.blog.service.Impl;

import cn.stapxs.blog.controller.ArticleController;
import cn.stapxs.blog.mapper.ArticleMapper;
import cn.stapxs.blog.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * @Description 保存文章预生成 HTML
     * @Date 下午 01:03 2022/05/10
     * @Param [artId, html]
     * @return void
    **/
    @Override
    public void uploadArticleHtml(String artId, String html) {
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
}
