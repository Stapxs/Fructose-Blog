package cn.stapxs.blog.service;

import cn.stapxs.blog.model.SortInfo;

public interface SortTagService {

    // Sort
    SortInfo[] getSortList();
    void addSort(SortInfo sortInfo);
    void deleteSort(String name);
}
