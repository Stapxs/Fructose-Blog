package cn.stapxs.blog.controller;

import cn.stapxs.blog.model.Article;
import cn.stapxs.blog.model.SortInfo;
import cn.stapxs.blog.service.SortTagService;
import cn.stapxs.blog.service.UserService;
import cn.stapxs.blog.util.View;
import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @Version: 1.0
 * @Date: 2022/05/09 下午 08:14
 * @ClassName: sortTagController
 * @Author: Stapxs
 * @Description PS：这是标签和分类一起的控制器（懒得拆开了）
 **/
@Controller
public class SortTagController {

    private static final Gson gson = new Gson();

    @Autowired
    SortTagService sortTagService;
    @Autowired
    UserService userService;

    @GetMapping(value = "api/sort/list", name = "获取分类列表")
    public String getSortList(Model model) {
        SortInfo[] sortList = sortTagService.getSortList();
        List<SortBack> sortBackList = new ArrayList<>();
        for(SortInfo sort : sortList) {
            sortBackList.add(new SortBack(sort, 0));
        }
        return View.api(200, "success", sortBackList, model);
    }

    @PostMapping(value = "api/sort/add", name = "添加分类")
    public String addSort(@RequestBody String info, int id, String token, Model model) {
        // 验证管理员身份
        if(userService.verifyAdministrator(id, token)) {
            SortInfo infoBody = gson.fromJson(info, SortInfo.class);
            // 分类名只能是英文
            if(!infoBody.getSort_name().matches("^[a-zA-Z]+$")) {
                return View.api(400, "Bad Request", "分类名只能是英文", model);
            }
            // 分类名不能是空白符号
            if(infoBody.getSort_name().matches("^\\s+$")) {
                return View.api(400, "Bad Request", "分类名不能是空白符号", model);
            }
            try {
                sortTagService.addSort(infoBody);
                return View.api(200, "success", "操作成功！", model);
            } catch (Exception e) {
                if(e.getMessage().contains("java.sql.SQLIntegrityConstraintViolationException")) {
                    return View.api(500, "Internal Server Error", "分类缩写重复", model);
                } else {
                    return View.api(500, "Internal Server Error", "未知错误！", model);
                }
            }
        } else {
            return View.api(403, "forbidden", "验证登录失败或无权限！", model);
        }
    }

    @PostMapping(value = "api/sort/delete", name = "删除分类")
    public String deleteSort(String name, int id, String token, Model model) {
        // 验证管理员身份
        if(userService.verifyAdministrator(id, token)) {
            try {
                sortTagService.deleteSort(name);
                return View.api(200, "success", "操作成功！", model);
            } catch (Exception e) {
                return View.api(500, "Internal Server Error", "操作失败！", model);
            }
        } else {
            return View.api(403, "forbidden", "验证登录失败或无权限！", model);
        }
    }

    @GetMapping(value = "api/sort/get/{name}", name = "获取分类下的所有文章")
    public String getSortArticles(@PathVariable String name, Model model) {
        try {
            Optional<List<Article>> articles = Optional.ofNullable(sortTagService.getArticleListBySort(name));
            if(articles.isPresent()) {
                return View.api(200, "success", articles.get(), model);
            } else {
                return View.api(404, "Not Found", "没有找到该分类或分类没有文章！", model);
            }
        } catch (Exception e) {
            return View.api(500, "Internal Server Error", "未知错误！", model);
        }
    }

    // --------------------------------------

    @Data
    @Builder
    static class SortBack {
        private SortInfo info;
        private int count;
    }

}
