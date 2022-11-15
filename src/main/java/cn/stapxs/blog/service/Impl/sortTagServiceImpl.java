package cn.stapxs.blog.service.Impl;

import cn.stapxs.blog.mapper.SortTagMapper;
import cn.stapxs.blog.model.Article;
import cn.stapxs.blog.model.SortInfo;
import cn.stapxs.blog.service.SortTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Version: 1.0
 * @Date: 2022/05/09 下午 08:18
 * @ClassName: SortTagServiceImpl
 * @Author: Stapxs
 * @Description TO DO
 **/
@Service
public class sortTagServiceImpl implements SortTagService {

    @Autowired
    SortTagMapper mapper;
    
    /**
     * @Author Stapxs
     * @Description 获取所有分类
     * @Date 下午 08:25 2022/05/09
     * @Param []
     * @return cn.stapxs.blog.model.SortInfo[]
    **/
    @Override
    public SortInfo[] getSortList() {
        return mapper.getSortTag();
    }

    @Override
    public SortInfo getSortInfo(String name) {
        return mapper.getSortInfo(name);
    }

    @Override
    public void addSort(SortInfo sortInfo) {
        mapper.addSort(sortInfo);
    }

    @Override
    public void deleteSort(String name) {
        mapper.deleteSort(name);
    }

    @Override
    public List<Article> getArticleListBySort(String name) {
        return mapper.getArticleBySort(name);
    }
}
