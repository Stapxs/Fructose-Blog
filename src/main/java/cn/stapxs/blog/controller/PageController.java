package cn.stapxs.blog.controller;

import cn.stapxs.blog.model.user.User;
import cn.stapxs.blog.service.ConfigService;
import cn.stapxs.blog.service.Impl.configServiceImpl;
import cn.stapxs.blog.service.UserService;
import cn.stapxs.blog.util.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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

    // 配置页索引
    @RequestMapping(value = {"/", "/config"})
    public String index(Model model) {
        System.out.println(config.getConfigStatue());
        if(config.getConfigStatue() == 0) {
            // 生成临时 token
            String token = config.createToken();
            if(token != null) {
                model.addAttribute("token", config.createToken());
                // 填充系统信息
                model.addAttribute("databaseStatue", config.getDataSourceStatue());
                model.addAttribute("sqlConfig", config.getDataConfig());
                model.addAttribute("sqlVer", config.getSQLVersion());
                model.addAttribute("sysInfo", config.getSystemInfo());
                // 跳转到初始化配置页面
                return "config";
            } else {
                return View.api(403, "Forbidden", "初始化配置已被触发，请在打开的页面内操作！", model);
            }
        } else {
            // 跳转到主页
            return "index";
        }
    }

    // 错误页索引
    @RequestMapping("/error")
    public String error(Model model) {
        configServiceImpl.SiteConfig siteInfo = config.getSiteConfig();
        model.addAttribute("siteName", siteInfo.getFb_name());
        return "error";
    }

    // 登录页索引
    @RequestMapping("/login")
    public String login(Model model) {
        return View.login(config.getSiteConfig(), null, model);
    }

    // 后台索引，此页面全前后端分离
    @RequestMapping(value = {"/admin", "/admin/{name}"})
    public String admin(@PathVariable Optional<String> name, @CookieValue(value = "id") Optional<Integer> userId, Model model) {
        // 无法获取登录 ID，跳转到登陆界面
        if(!userId.isPresent()) {
            return View.login(config.getSiteConfig(), "admin", model);
        }

        // 加载后台主页
        Optional<User> info = Optional.ofNullable(user.getUser(userId.get()));
        // 传递账户类型
        if(info.isPresent()) {
            model.addAttribute("userType", info.get().getAccount_type());
        } else {
            return View.login(config.getSiteConfig(), "admin", model);
        }
        // 其他数据
        configServiceImpl.SiteConfig siteInfo = config.getSiteConfig();
        model.addAttribute("siteName", siteInfo.getFb_name());
        model.addAttribute("userInfo", info);
        // 返回
        return name.map(s -> "admin/" + s).orElse("admin/index");
    }
}
