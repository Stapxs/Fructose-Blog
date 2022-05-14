package cn.stapxs.blog.controller;

import cn.stapxs.blog.model.Article;
import cn.stapxs.blog.model.user.User;
import cn.stapxs.blog.model.user.UserInfo;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

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

    @PostMapping(value = "api/article/update", name = "更新文章")
    public String updateArticle(
            int id, String token, String art_id, String title, String link, @RequestBody String content, Model model) {
        // 验证登录
        if (userService.verifyLogin(id, token)) {
            // 验证文章所有权
            if (articleService.verifyArticle(id, art_id)) {
                // 标题和正文不能是空白
                if (title.trim().isEmpty() || content.trim().isEmpty()) {
                    return View.api(400, "Bad Request", "标题不能是空白字符", model);
                }
                // 提交数据
                try {
                    articleService.updateArticle(art_id, title, link, content);
                    return View.api(200, "success", art_id, model);
                } catch (Exception e) {
                    e.printStackTrace();
                    return View.api(500, "Internal Server Error", e.getMessage(), model);
                }
            } else {
                return View.api(403, "Forbidden", "无权操作，可能是因为这不是你的文章！", model);
            }
        } else {
            return View.api(403, "Forbidden", "验证登录失败！", model);
        }
    }

    @PostMapping(value = "api/article/delete", name = "删除文章")
    public String deleteArticle(int id, String token, String art_id, Model model) {
        // 验证登录
        if (userService.verifyLogin(id, token)) {
            // 验证文章所有权
            if (articleService.verifyArticle(id, art_id)) {
                // 提交数据
                try {
                    articleService.deleteArticle(art_id);
                    return View.api(200, "success", "操作成功！", model);
                } catch (Exception e) {
                    e.printStackTrace();
                    return View.api(500, "Internal Server Error", e.getMessage(), model);
                }
            } else {
                return View.api(403, "Forbidden", "无权操作，可能是因为这不是你的文章！", model);
            }
        } else {
            return View.api(403, "Forbidden", "验证登录失败！", model);
        }
    }

    @PostMapping(value = "api/article/upload/html", name = "上传预生成的 HTML")
    public String uploadArticleHtml(int id, String token, String artId, @RequestBody String html, Model model) {
        // 验证登录
        if (userService.verifyLogin(id, token)) {
            // 验证文章所有权
            if (articleService.verifyArticle(id, artId)) {
                // 提交数据
                try {
                    articleService.updateArticleHtml(artId, html);
                    return View.api(200, "success", "操作成功！", model);
                } catch (Exception e) {
                    e.printStackTrace();
                    return View.api(500, "Internal Server Error", e.getMessage(), model);
                }
            } else {
                return View.api(403, "Forbidden", "无权操作，可能是因为这不是你的文章！", model);
            }
        } else {
            return View.api(403, "Forbidden", "验证登录失败！", model);
        }
    }

    @PostMapping(value = "api/article/upload/option", name = "更新文章选项设置")
    public String uploadArticleOption(int id, String token, String artId, @RequestBody String option, Model model) {
        // 验证登录
        if (userService.verifyLogin(id, token)) {
            // 验证文章所有权
            if (articleService.verifyArticle(id, artId)) {
                // 提交数据
                try {
                    OptionInfo optionInfo = gson.fromJson(option, OptionInfo.class);
                    articleService.updateArticleOption(artId, optionInfo);
                    return View.api(200, "success", "操作成功！", model);
                } catch (Exception e) {
                    e.printStackTrace();
                    return View.api(500, "Internal Server Error", e.getMessage(), model);
                }
            } else {
                return View.api(403, "Forbidden", "无权操作，可能是因为这不是你的文章！", model);
            }
        } else {
            return View.api(403, "Forbidden", "验证登录失败！", model);
        }
    }


    @GetMapping(value = "api/article/list", name = "获取所有文章基本信息")
    public String getArticleList(Model model) {
        try {
            Optional<List<Article>> article = Optional.ofNullable(articleService.getArticleSummaryList());
            if (article.isPresent()) {
                // 遍历列表
                List<ArticleSummaryInfo> articleSummaryInfoList = new ArrayList<>();
                for (Article a : article.get()) {
                    // 获取作者信息
                    Optional<User> user = Optional.ofNullable(userService.getUser(a.getUser_id()));
                    Optional<UserInfo> userInfo = Optional.ofNullable(userService.getUserInfo(a.getUser_id()));
                    if (user.isPresent() && userInfo.isPresent()) {
                        // 处理摘要
                        String summary = a.getArt_html();
                        // 去除 html 标签
                        summary = summary.replaceAll("<[^>]+>", "");
                        summary = summary.replaceAll("\\s*", "");
                        summary = summary.substring(0, Math.min(summary.length(), 300));
                        a.setArt_html(summary);
                        // 构建信息
                        ArticleSummaryInfo articleSummaryInfo = new ArticleSummaryInfo();
                        articleSummaryInfo.setUser_name(user.get().getUser_name());
                        articleSummaryInfo.setUser_nick(userInfo.get().getUser_nick());
                        articleSummaryInfo.setUser_link(userInfo.get().getUser_link());
                        articleSummaryInfo.setArticle(a);
                        // 添加
                        articleSummaryInfoList.add(articleSummaryInfo);
                    } else {
                        return View.api(500, "Internal Server Error", "获取用户信息失败！", model);
                    }
                }
                return View.api(200, "success", articleSummaryInfoList, model);
            } else {
                return View.api(404, "Not Found", "没有文章！", model);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return View.api(500, "Internal Server Error", e.getMessage(), model);
        }
    }

    @GetMapping(value = {"api/article/{id}", "api/article/{id}/{option}"}, name = "获取完整文章信息")
    public String getArticle(@PathVariable("id") String id, @PathVariable("option") Optional<String> option, Model model) {
        try {
            Optional<Article> article = Optional.ofNullable(articleService.getArticle(id));
            if(article.isPresent()) {
                Article back = article.get();
                if(option.isPresent()) {
                    // 返回控制（减少返回体积）
                    switch (option.get()) {
                        case "md": back.setArt_html(null); break;
                        case "html": back.setArt_markdown(null); break;
                    }
                }
                return View.api(200, "success", back, model);
            } else {
                return View.api(404, "Not Found", "没有找到这篇文章！", model);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return View.api(500, "Internal Server Error", e.getMessage(), model);
        }
    }

    @GetMapping(value = {"api/article/img/{id}", "/api/article/img/{id}/{type}"}, name = "获取文章第一张图片")
    public String getArtImg(@PathVariable("id") String id, @PathVariable Optional<String> type, HttpServletResponse response, Model model) {
        try {
            Optional<Article> article = Optional.ofNullable(articleService.getArticle(id));
            if(article.isPresent()) {
                String art_html = article.get().getArt_html();
                // 获取第一个 img 标签的 src 属性
                String imgSrc = art_html.split("<img")[1].split("src=\"")[1].split("\"")[0];
                if(imgSrc.startsWith("http")) {
                    if(type.isPresent()) {
                        if(type.get().equals("img")) {
                            response.sendRedirect(imgSrc);
                            return null;
                        } else if(type.get().equals("url")) {
                            model.addAttribute("code", "200");
                            model.addAttribute("str", imgSrc);
                            return "api";
                        } else {
                            return View.api(200, "success", imgSrc, model);
                        }
                    } else {
                        return View.api(200, "success", imgSrc, model);
                    }
                } else {
                    if(type.isPresent()) {
                        if (type.get().equals("url") || type.get().equals("img")) {
                            model.addAttribute("code", "404");
                            model.addAttribute("str", "");
                            return "api";
                        } else {
                            return View.api(404, "Not Found", "没有找到图片！", model);
                        }
                    } else {
                        return View.api(404, "Not Found", "没有找到图片！", model);
                    }
                }
            } else {
                return View.api(404, "Not Found", "没有找到这篇文章！", model);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return View.api(500, "Internal Server Error", e.getMessage(), model);
        }
    }

    // --------------------------------------

    @Data
    @Builder
    public static class OptionInfo {
        private Date art_date;
        private String art_link;
        private String art_sort;
        private String art_tag;
        private String art_quote;
        private int art_statue;
        private String art_appendix;
    }

    @Data
    public static class ArticleSummaryInfo {
        private String user_name;
        private String user_nick;
        private String user_link;
        private Article article;
        private String art;
    }
}
