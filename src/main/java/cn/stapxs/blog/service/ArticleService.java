package cn.stapxs.blog.service;

import cn.stapxs.blog.controller.ArticleController;
import cn.stapxs.blog.model.Article;

import java.util.List;

public interface ArticleService {

    boolean verifyArticle(int user_id, String art_id);

    String uploadArticle(int user_id, String title, String link, String content);
    void updateArticle(String art_id, String title, String link, String content);
    void deleteArticle(String art_id);
    void updateArticleHtml(String artId, String html);
    void updateArticleOption(String artId, ArticleController.OptionInfo optionInfo);

    String getIDByLink(String link);
    List<Article> getArticleSummaryList();
    Article getArticle(String artId);

}
