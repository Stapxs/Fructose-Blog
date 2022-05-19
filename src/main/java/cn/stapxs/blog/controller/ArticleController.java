package cn.stapxs.blog.controller;

import cn.stapxs.blog.model.Article;
import cn.stapxs.blog.model.FileInfo;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.net.URLDecoder;
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


    @GetMapping(value = {"api/article/list", "api/article/list/{sort}"}, name = "获取所有文章基本信息")
    public String getArticleList(@PathVariable Optional<String> sort, Model model) {
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
                        // 添加，只包括公开的文章
                        if(a.getArt_statue() == 1) {
                            // 如果 sort 不是空，则仅包含含有 sort 的文章
                            if (sort.isPresent()) {
                                if (a.getArt_sort().equals(sort.get())) {
                                    articleSummaryInfoList.add(articleSummaryInfo);
                                }
                            } else {
                                articleSummaryInfoList.add(articleSummaryInfo);
                            }
                        }
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

    @GetMapping(value = {"api/article/get/{id}", "api/article/get/{id}/{option}"}, name = "获取完整文章信息")
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

    @GetMapping(value = {"api/article/img/get/{id}", "/api/article/img/get/{id}/{type}"}, name = "获取文章第一张图片")
    public String getArtImg(@PathVariable("id") String id, @PathVariable Optional<String> type, HttpServletResponse response, Model model) {
        try {
            Optional<Article> article = Optional.ofNullable(articleService.getArticle(id));
            if(article.isPresent()) {
                String art_html = article.get().getArt_html();
                // URL 解码
                art_html = URLDecoder.decode(art_html, "UTF-8");
                // 获取第一个 img 标签的 src 属性
                String imgSrc = art_html.split("<img")[1].split("src=\"")[1].split("\"")[0];
                if (type.isPresent()) {
                    if (type.get().equals("img")) {
                        response.sendRedirect(imgSrc);
                        return null;
                    } else if (type.get().equals("url")) {
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
                return View.api(404, "Not Found", "没有找到这篇文章！", model);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return View.api(500, "Internal Server Error", e.getMessage(), model);
        }
    }

    // --------------------------------------

    @PostMapping(value = "api/article/img/upload", name = "上传文章图片")
    public String uploadArtImg(int id, String token, @RequestParam("uploadFile") MultipartFile uploadFile, HttpSession session, Model model) {
        try {
            // 验证登录
            if (userService.verifyLogin(id, token)) {
                // 文件为空
                if (uploadFile.isEmpty()) {
                    model.addAttribute("code", "403");
                    model.addAttribute("str", "{\"success\":0,\"message\":\"请上传文件。\"}");
                    return "api";
                }
                // 保存文件
                String basePath = session.getServletContext().getRealPath("/") + "web/images/upload/";

                Calendar calendar = Calendar.getInstance();
                String year = calendar.get(Calendar.YEAR) + "";
                String month = calendar.get(Calendar.MONTH) + "";
                String uploadTargetPath = basePath + year + month + "/";

                String originalFileName = uploadFile.getOriginalFilename();
                String fileType = originalFileName.substring(originalFileName.indexOf(".") + 1);
                String newFileName = UUID.randomUUID() + "." + fileType;
                File targetFile = new File(uploadTargetPath, newFileName);

                if (!targetFile.exists()) {
                    new File(uploadTargetPath).mkdirs();
                }

                // 保存图片信息
                FileInfo fileInfo = new FileInfo();
                fileInfo.setUser_id(id);
                fileInfo.setFile_name(originalFileName);
                fileInfo.setFile_size(uploadFile.getSize());
                fileInfo.setFile_url("/images/upload/" + year + month + "/" + newFileName);
                articleService.saveArticleImg(fileInfo);
                // 移动图片
                uploadFile.transferTo(targetFile);
                // 返回
                model.addAttribute("code", "200");
                model.addAttribute("str", "{\"success\":1,\"message\":\"保存成功!\",\"url\":\"/images/upload/" + year + month + "/" + newFileName + "\"}");
                return "api";
            } else {
                model.addAttribute("code", "403");
                model.addAttribute("str", "{\"success\":0,\"message\":\"验证登陆失败！\"}");
                return "api";
            }
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("code", "500");
            model.addAttribute("str", "{\"success\":0,\"message\":\"" + e.getMessage() + "\"}");
            return "api";
        }
    }

    @GetMapping(value = "api/article/img/list", name = "获取文件列表")
    public String getArtImgList(Model model) {
        try {
            Optional<List<FileInfo>> files = Optional.ofNullable(articleService.getArticleFileList());
            if (files.isPresent()) {
                return View.api(200, "success", files, model);
            } else {
                return View.api(404, "Not Found", "没有文件！", model);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return View.api(500, "Internal Server Error", e.getMessage(), model);
        }
    }

    @PostMapping(value = "api/article/img/delete", name = "删除文件")
    public String deleteArtImg(int id, String token, String name, Model model) {
        try {
            // 验证管理员权限
            if (userService.verifyAdministrator(id, token)) {
                articleService.deleteArticleImg(name);
                return View.api(200, "success", "操作成功！", model);
            } else {
                return View.api(403, "Forbidden", "验证登陆失败或没有权限！", model);
            }
        } catch (Exception ex) {
            return View.api(500, "Internal Server Error", ex.getMessage(), model);
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
