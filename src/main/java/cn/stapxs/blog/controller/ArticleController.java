package cn.stapxs.blog.controller;

import cn.stapxs.blog.service.ArticleService;
import cn.stapxs.blog.service.UserService;
import cn.stapxs.blog.util.View;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

/**
 * @Version: 1.0
 * @Date: 2022/05/10 上午 09:34
 * @ClassName: articleController
 * @Author: Stapxs
 * @Description 文章相关操作
 **/
@Controller
public class ArticleController {

    private static final Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    @Autowired
    UserService userService;
    @Autowired
    ArticleService articleService;

    @PostMapping(value = "api/article/upload", name = "上传文章")
    public String uploadArticle(
            int id, String token, String title, Optional<String> link, @RequestBody String content, Model model) {
        // PS：保存文章的时候优先保存必要数据，包括标题，链接和正文，其他的由前端二次上传
        // 验证登录
        if (userService.verifyLogin(id, token)) {
            // 标题和正文不能是空白
            if (title.trim().isEmpty() || content.trim().isEmpty()) {
                return View.api(400, "Bad Request", "标题不能是空白字符", model);
            }
            // 如果链接不存在则根据当前日期生成
            if (!link.isPresent()) {
                link = Optional.of(new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(Calendar.getInstance().getTime()));
            }
            // 提交数据
            try {
                String artId = articleService.uploadArticle(id, title, link.get(), content);
                return View.api(200, "success", artId, model);
            } catch (Exception e) {
                e.printStackTrace();
                return View.api(500, "Internal Server Error", e.getMessage(), model);
            }
        } else {
            return View.api(403, "Forbidden", "验证登录失败！", model);
        }
    }

    @PostMapping(value = "api/article/upload/html", name = "上传预约生成的 HTML")
    public String uploadArticleHtml(int id, String token, String artId, @RequestBody String html, Model model) {
        // 验证登录
        if (userService.verifyLogin(id, token)) {
            // 提交数据
            try {
                articleService.uploadArticleHtml(artId, html);
                return View.api(200, "success", "操作成功！", model);
            } catch (Exception e) {
                e.printStackTrace();
                return View.api(500, "Internal Server Error", e.getMessage(), model);
            }
        } else {
            return View.api(403, "Forbidden", "验证登录失败！", model);
        }
    }

    @PostMapping(value = "api/article/upload/option", name = "更新文章选项设置")
    public String uploadArticleOption(int id, String token, String artId, @RequestBody String option, Model model) {
        // 验证登录
        if (userService.verifyLogin(id, token)) {
            // 提交数据
            try {
                System.out.println(option);
                OptionInfo optionInfo = gson.fromJson(option, OptionInfo.class);
                articleService.updateArticleOption(artId, optionInfo);
                return View.api(200, "success", "操作成功！", model);
            } catch (Exception e) {
                e.printStackTrace();
                return View.api(500, "Internal Server Error", e.getMessage(), model);
            }
        } else {
            return View.api(403, "Forbidden", "验证登录失败！", model);
        }
    }

    // --------------------------------------

    @Data
    @Builder
    public static class OptionInfo {
        private Date art_date;
        private String art_link;
        private String[] art_sort;
        private String[] art_tag;
        private String art_quote;
        private int art_statue;
    }
}
