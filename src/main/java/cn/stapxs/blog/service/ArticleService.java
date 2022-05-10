package cn.stapxs.blog.service;

import cn.stapxs.blog.controller.ArticleController;

public interface ArticleService {

    String uploadArticle(int user_id, String title, String link, String content);
    void uploadArticleHtml(String artId, String html);

    void updateArticleOption(String artId, ArticleController.OptionInfo optionInfo);

}
