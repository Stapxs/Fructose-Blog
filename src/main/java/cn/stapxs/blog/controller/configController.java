package cn.stapxs.blog.controller;

import cn.stapxs.blog.service.ConfigService;
import cn.stapxs.blog.service.Impl.configServiceImpl;
import cn.stapxs.blog.service.UserService;
import cn.stapxs.blog.util.View;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Version: 1.0
 * @Date: 2022/05/05 下午 05:53
 * @ClassName: configController
 * @Author: Stapxs
 * @Description TO DO
 **/
@Controller
public class configController {

    private static final Gson gson = new Gson();

    @Autowired
    ConfigService configService;
    @Autowired
    UserService userService;

    @GetMapping(value = "api/config/string/{name}", name = "获取指定的配置")
    public String getConfig(@PathVariable("name") String name, Model model) {
        try {
            return View.api(200, "success", configService.getStringConfig(name), model);
        } catch (Exception e) {
            e.printStackTrace();
            return View.api(500, "Internal Server Error", e.getMessage(), model);
        }
    }

    @PostMapping(value = "api/config/update/string/{name}", name = "更新指定的配置")
    public String updateConfig(int id, String token, @PathVariable String name, @RequestBody String body, Model model) {
        // 验证管理员权限
        if(userService.verifyAdministrator(id, token)) {
            try {
                configService.updateStringConfig(name, body);
                return View.api(200, "success", "更新成功", model);
            } catch (Exception e) {
                e.printStackTrace();
                return View.api(500, "Internal Server Error", e.getMessage(), model);
            }
        } else {
            return View.api(403, "Forbidden", "验证登陆失败或权限不足！", model);
        }
    }
}
