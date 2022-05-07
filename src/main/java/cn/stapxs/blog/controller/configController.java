package cn.stapxs.blog.controller;

import cn.stapxs.blog.service.ConfigService;
import cn.stapxs.blog.service.Impl.configServiceImpl;
import cn.stapxs.blog.util.View;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    /**
     * @Author Stapxs
     * @Description 设置站点设置
     * @Date 下午 08:55 2022/05/05
     * @Param [json, request, model]
     * @return java.lang.String
    **/
    @PostMapping("api/site/config")
    public String configSite(@RequestBody String json, HttpServletRequest request, Model model) {
        String authorization = request.getHeader("Authorization");
        // 验证 Authorization header
        if (authorization.equals("Bearer " + configService.getToken())) {
            // 解析 json
            configServiceImpl.SiteConfig siteConfig = gson.fromJson(json, configServiceImpl.SiteConfig.class);
            // 更新数据库
            try {
                configService.updateSiteConfig(siteConfig);
                return View.api(200, "success", "操作成功", model);
            } catch (Exception e) {
                return View.api(500, "Internal Server Error", "数据库更新失败", model);
            }
        } else {
            return View.api(401, "Unauthorized", "未授权", model);
        }
    }

}
