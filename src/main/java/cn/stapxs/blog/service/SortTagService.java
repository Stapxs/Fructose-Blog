package cn.stapxs.blog.service;

import cn.stapxs.blog.model.Article;
import cn.stapxs.blog.model.SortInfo;

import java.util.List;

public interface SortTagService {

    // Sort
    SortInfo[] getSortList();
    SortInfo getSortInfo(String name);
    void addSort(SortInfo sortInfo);
    void deleteSort(String name);
    List<Article> getArticleListBySort(String name);
}
