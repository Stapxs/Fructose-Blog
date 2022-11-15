package cn.stapxs.blog.controller;

import cn.stapxs.blog.model.Article;
import cn.stapxs.blog.model.Config;
import cn.stapxs.blog.model.SortInfo;
import cn.stapxs.blog.model.user.User;
import cn.stapxs.blog.model.user.UserInfo;
import cn.stapxs.blog.service.ArticleService;
import cn.stapxs.blog.service.ConfigService;
import cn.stapxs.blog.service.SortTagService;
import cn.stapxs.blog.service.UserService;
import cn.stapxs.blog.util.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

/**
 * @Version: 1.0
 * @Date: 2022/02/22 下午 04:17
 * @ClassName: PageController
 * @Author: Stapxs
 * @Description TO DO
 **/
@Controller
public class PageController {

    @Autowired
    ConfigService config;
    @Autowired
    UserService user;
    @Autowired
    ArticleService article;
    @Autowired
    SortTagService sortTag;

    // 主页配置页索引
    @RequestMapping(value = {"/", "/page/{page}"})
    public String index(@PathVariable Optional<Integer> page, Model model) {
        if(config.getConfigStatue() == 0) {
            // 生成临时 token
//            String token = config.createToken();
//            if(token != null) {
//                model.addAttribute("token", config.createToken());
//                // 填充系统信息
//                model.addAttribute("databaseStatue", config.getDataSourceStatue());
//                model.addAttribute("sqlConfig", config.getDataConfig());
//                model.addAttribute("sqlVer", config.getSQLVersion());
//                model.addAttribute("sysInfo", config.getSystemInfo());
//                // 跳转到初始化配置页面
//                return "config";
//            } else {
//                return View.api(403, "Forbidden", "初始化配置已被触发，请在打开的页面内操作！", model);
//            }
            return "404";
        } else {
            if(!page.isPresent()) { page = Optional.of(1); }
            // 跳转到主页, 主页需要传递站点设置，文章列表，分类列表，附加设置
            Optional<Config> configInfo = Optional.ofNullable(config.getConfig());
            List<Article> allArticleList = article.getArticleSummaryList();
            List<Article> articleList = article.getArticleSummaryList((page.get() - 1) * 5);
            int articleCount = article.getArticleCount();
            if(articleList.size() == 0) {
                // 重定向到第一页
                return "redirect:/page/" + (int)Math.ceil(articleCount / 5.0);
            }
            if(configInfo.isPresent() && configInfo.get().getCfg_theme() != null) {
                model.addAttribute("config", configInfo.get());
                model.addAttribute("articles", allArticleList);
                model.addAttribute("now-page-articles", articleList);
                model.addAttribute("sort", sortTag.getSortList());
                model.addAttribute("page", page.get());
                model.addAttribute("max-page", (int)Math.ceil(articleCount / 5.0));
                // TODO tag 还没写
                return "theme/" + configInfo.get().getCfg_theme() + "/index";
            } else {
                return View.api(404, "Not Found", "获取主页设置失败 ……", model);
            }
        }
    }

    // 文章详情页索引
    @RequestMapping("/article/{link}")
    public String article(@PathVariable("link") String link, Model model) {
        // 根据链接获取文章信息
        Optional<Article> articleInfo = Optional.ofNullable(article.getArticle(article.getIDByLink(link)));
        if(articleInfo.isPresent()) {
            Optional<UserInfo> userInfo = Optional.ofNullable(user.getUserInfo(articleInfo.get().getUser_id()));
            if (userInfo.isPresent()) {
                model.addAttribute("config", config.getConfig());
                model.addAttribute("sort", sortTag.getSortList());
                model.addAttribute("article", articleInfo.get());
                model.addAttribute("user", userInfo.get());
                return "theme/" + config.getConfig().getCfg_theme() + "/article";
            }
        } else {
            return "404";
        }
        return View.api(500, "Internal Server Error", "页面路由失败 ……", model);
    }

    // 分类页索引
    @RequestMapping(value = {"/category", "/category/{name}"})
    public String category(@PathVariable Optional<String> name, Model model) {
        // 获取分类信息
        if(name.isPresent()) {
            String categoryName = name.get().substring(0, 1).toUpperCase() + name.get().substring(1);
            Optional<SortInfo> sortInfo = Optional.ofNullable(sortTag.getSortInfo(categoryName));
            Optional<List<Article>> articles = Optional.ofNullable(sortTag.getArticleListBySort(categoryName));
            if(sortInfo.isPresent() && articles.isPresent()) {
                model.addAttribute("config", config.getConfig());
                model.addAttribute("sort", sortTag.getSortList());
                model.addAttribute("now-sort", sortInfo.get());
                model.addAttribute("articles", articles.get());
                return "theme/" + config.getConfig().getCfg_theme() + "/category";
            } else {
                return View.api(404, "Not Found", "获取分类信息失败 ……", model);
            }
        } else {
            return View.api(500, "Internal Server Error", "页面路由失败 ……", model);
        }
    }

    // ---------------------------------------------------------------

    // 错误页索引
    @RequestMapping("/error")
    public String error(Model model) {
        Config configInfo = config.getConfig();
        model.addAttribute("siteName", configInfo.getFb_name());
        return "error";
    }
    // 登录页索引
    @RequestMapping("/login")
    public String login(Model model) {
        return View.login(config.getConfig(), null, model);
    }
    // 后台索引
    @RequestMapping(value = {"/admin", "/admin/{name}", "/admin/{name}/{value}"})
    public String admin(@PathVariable Optional<String> name, @PathVariable Optional<String> value, @CookieValue(value = "id") Optional<Integer> userId, Model model) {
        Config configInfo = config.getConfig();
        // 无法获取登录 ID，跳转到登陆界面
        if(!userId.isPresent()) {
            return View.login(configInfo, "admin", model);
        }
        // 加载后台主页
        Optional<User> info = Optional.ofNullable(user.getUser(userId.get()));
        // 传递账户类型
        if(info.isPresent()) {
            model.addAttribute("userType", info.get().getAccount_type());
        } else {
            return View.login(configInfo, "admin", model);
        }
        // 其他数据
        model.addAttribute("siteConfig", configInfo);
        model.addAttribute("userInfo", info);
        value.ifPresent(s -> model.addAttribute("value", s));
        // 返回
        return name.map(s -> "admin/" + s).orElse("admin/index");
    }
}
