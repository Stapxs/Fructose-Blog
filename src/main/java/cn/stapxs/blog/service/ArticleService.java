package cn.stapxs.blog.service;

import cn.stapxs.blog.controller.ArticleController;
import cn.stapxs.blog.model.Article;
import cn.stapxs.blog.model.FileInfo;

import java.util.List;

public interface ArticleService {

    boolean verifyArticle(int user_id, String art_id);

    String uploadArticle(int user_id, String title, String link, String content);
    void updateArticle(String art_id, String title, String link, String content);
    void deleteArticle(String art_id);
    void updateArticleHtml(String artId, String html);
    void updateArticleOption(String artId, ArticleController.OptionInfo optionInfo);

    void saveArticleImg(FileInfo fileInfo);
    void deleteArticleImg(String name);

    String getIDByLink(String link);
    int getArticleCount();
    List<Article> getArticleSummaryList();
    List<Article> getArticleSummaryList(int page);
    Article getArticle(String artId);
    List<FileInfo> getArticleFileList();

}
