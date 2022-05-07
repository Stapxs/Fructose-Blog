package cn.stapxs.blog.util;

import cn.stapxs.blog.model.Back;
import cn.stapxs.blog.service.ConfigService;
import cn.stapxs.blog.service.Impl.configServiceImpl;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

/**
 * @Version: 1.0
 * @Date: 2022/05/01 下午 12:43
 * @ClassName: View
 * @Author: Stapxs
 * @Description API 跳转封装
 **/
public class View {

    private static final Gson gson = new Gson();

    /**
     * @Author Stapxs
     * @Description API 跳转主实现
     * @Date 下午 12:44 2022/05/01
     * @Param [code, msg, data, model]
     * @return java.lang.String
    **/
    private static String jump(int code, String msg, String str, Model model) {
        model.addAttribute("code", String.valueOf(code));
        model.addAttribute("str", gson.toJson(new Back(code, msg, str, null)));
        return "api";
    }
    private static String jump(int code, String msg, Object data, Model model) {
        model.addAttribute("code", String.valueOf(code));
        model.addAttribute("str", gson.toJson(new Back(code, msg, null, data)));
        return "api";
    }

    public static String api(Object data, Model model) {
        if(data instanceof String) {
            return jump(200, "success", data.toString(), model);
        } else {
            return jump(200, "success", data, model);
        }
    }
    public static String api(int code, String msg, Object data, Model model) {
        if(data instanceof String) {
            return jump(code, msg, data.toString(), model);
        } else {
            return jump(code, msg, data, model);
        }
    }

    /**
     * @Author Stapxs
     * @Description 跳转到登录页
     * @Date 上午 10:04 2022/05/07
     * @Param [siteInfo, back, model]
     * @return java.lang.String
    **/
    public static String login(configServiceImpl.SiteConfig siteInfo, String back, Model model) {
        // 传递设置
        model.addAttribute("siteName", siteInfo.getFb_name());
        model.addAttribute("allowReg", siteInfo.isCfg_allow_reg());
        if(back != null) {
            model.addAttribute("back", back);
        }
        return "login";
    }
}
