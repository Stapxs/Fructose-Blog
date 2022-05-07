package cn.stapxs.blog.controller;

import cn.stapxs.blog.model.user.User;
import cn.stapxs.blog.model.user.UserInfo;
import cn.stapxs.blog.service.ConfigService;
import cn.stapxs.blog.service.Impl.configServiceImpl;
import cn.stapxs.blog.service.UserService;
import cn.stapxs.blog.util.Network;
import cn.stapxs.blog.util.PBKDF2;
import cn.stapxs.blog.util.RSAEncrypt;
import cn.stapxs.blog.util.View;
import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.crypto.BadPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Optional;

/**
 * @Version: 1.0
 * @Date: 2022/02/24 下午 03:50
 * @ClassName: accountController
 * @Author: Stapxs
 * @Description 账户相关功能
 **/
@Controller
public class accountController {

    public static Gson gson = new Gson();

    @Autowired
    UserService userService;
    @Autowired
    ConfigService configService;

    // -------------------------------------------------
    // 登录服务相关

    @GetMapping(value = "api/account/key/{name}", name = "获取登录需要的加密公钥")
    public String getLoginKey(@PathVariable String name, Model model) {
        try {
            Optional<User> user = Optional.ofNullable(userService.getUser(name));
            if (user.isPresent()) {
                // 生成 key
                String key = userService.getKey(user.get().getUser_id());
                return View.api(new keyInfo(user.get().getUser_id(), key), model);
            } else {
                return View.api(404, "Not Found", "没有这个账号", model);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return View.api(500, "Internal Server Error", ex.getMessage(), model);
        }
    }

    @PostMapping(value = "api/account/login", name = "登录")
    public String loginAccount(int id, String str, HttpServletRequest request, Model model) {
        str = str.replace(" ", "+");
        Optional<User> user = Optional.ofNullable(userService.getUser(id));
        if(user.isPresent()) {
            boolean passLogin;
            // 解码密码
            try {
                RSAEncrypt encrypt = new RSAEncrypt();
                str = encrypt.decrypt(str, user.get().getLogin_key());
                // 验证密码
                PBKDF2 pbkdf2 = new PBKDF2(32, 64, 30000);
                passLogin = pbkdf2.verify(str, user.get().getUser_password());
            } catch (BadPaddingException be) {
                be.printStackTrace();
                // RSA 解密失败
                return View.api(400, "Bad Request", "账号或密码无效", model);
            } catch (NullPointerException ne) {
                ne.printStackTrace();
                // 未申请公钥
                return View.api(403, "Forbidden", "无效的登陆操作", model);
            } catch (Exception ex) {
                ex.printStackTrace();
                // 其他错误
                return View.api(500, "Internal Server Error", ex.getMessage(), model);
            }
            // 后续流程
            if(passLogin) {
                // 删除私钥
                userService.delKey(user.get().getUser_id());
                // 生成返回 token
                Object back = new keyInfo(user.get().getUser_id(), user.get().createToken());
                // 保存 token
                userService.saveToken(user.get().getUser_id(), user.get().getUser_token());
                // 刷新登录信息
                String ip = Network.getIP(request);
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
                userService.saveLoginIP(id, ip);
                // 返回
                return View.api(200, "success", back, model);
            } else {
                return View.api(403, "Forbidden", "账号或密码错误", model);
            }
        } else {
            return View.api(404, "Not Found", "没有这个账号", model);
        }
    }

    @PostMapping(value = "api/account/register", name = "注册")
    public String registerAccount(String name, String email, String password, HttpServletRequest request, Model model) {
        try {
            Optional<User> user = Optional.ofNullable(userService.getUser(email));
            Optional<User> userByMail = Optional.ofNullable(userService.getUserByMail(email));
            if (user.isPresent() || userByMail.isPresent()) {
                return View.api(409, "Conflict", "账号已存在或邮箱已被注册", model);
            } else {
                // 获取 IP
                String ip = Network.getIP(request);
                if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                    ip = request.getRemoteAddr();
                }
                // 验证用户名合法性（只允许字母下划线）
                if (!name.matches("^[a-zA-Z_]+$")) {
                    return View.api(400, "Bad Request", "用户名只允许字母下划线", model);
                }
                // 验证邮箱合法性
                if (!email.matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$")) {
                    return View.api(400, "Bad Request", "邮箱格式不正确", model);
                }
                // 验证密码合法性(允许特殊符号）
                if (!password.matches("^[a-zA-Z0-9_\\-\\.\\,\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\+\\=\\{\\}\\[\\]\\|\\;\\:\\<\\>\\?]{6,32}$")) {
                    return View.api(400, "Bad Request", "密码格式不正确", model);
                }
                // 加密密码
                PBKDF2 pbkdf2 = new PBKDF2(32, 64, 30000);
                password = pbkdf2.getSaveStr(password);
                // 创建用户
                User newUser = new User();
                newUser.setUser_id(userService.getUserCount());
                newUser.setUser_name(name);
                newUser.setUser_mail(email);
                newUser.setUser_password(password);
                newUser.setReg_ip(ip);
                // 保存用户
                userService.saveUser(newUser);
                // 返回
                return View.api(200, "success", "注册成功", model);
            }
        }catch (Exception ex) {
            return View.api(500, "Internal Server Error", ex.getMessage(), model);
        }
    }

    // -------------------------------------------------
    // 用户信息相关
    @GetMapping(value = "api/account/avatar/{id}", name = "获取用户头像链接")
    public String getUserAvatar(@PathVariable int id, HttpServletResponse response, Model model) throws IOException, SQLException {
        // 此接口不需要用户认证
        Optional<UserInfo> userInfo = Optional.ofNullable(userService.getUserInfo(id));
        if (userInfo.isPresent()) {
            if (userInfo.get().getUser_avatar() == null) {
                // 没有自定头像，根据邮箱返回
                Optional<User> user = Optional.ofNullable(userService.getUser(id));
                if (user.isPresent()) {
                    String mail = user.get().getUser_mail();
                    // QQ 邮箱
                    if (mail.matches("^\\d*@qq.com$")) {
                        // 取出 @ 前的部分
                        String qq = mail.substring(0, mail.indexOf("@"));
                        return View.api(200, "success",
                                new avatarInfo(true, "https://q1.qlogo.cn/g?b=qq&s=0&nk=" + qq)
                                , model);
                    } else {
                        // 其他情况尝试从 gravatar 获取
                        configServiceImpl.SiteConfig siteConfig = configService.getSiteConfig();
                        String gravatarUrl = "https://www.gravatar.com/avatar/";
                        if (siteConfig.getImg_gravatar() != null) {
                            gravatarUrl = siteConfig.getImg_gravatar();
                        }
                        gravatarUrl = gravatarUrl + DigestUtils.md5Hex(mail.toLowerCase()) + "?s=200";
                        return View.api(200, "success",
                                new avatarInfo(false, gravatarUrl)
                                , model);
                    }
                } else {
                    return View.api(404, "Not Found", "用户不存在", model);
                }
            } else {
                // 返回 JSON
                return View.api(200, "success",
                        new avatarInfo(false, "/api/account/avatar/img/" + id)
                        , model);
            }
        } else {
            return View.api(404, "not found", "获取用户信息失败！", model);
        }
    }

    @Data
    @Builder
    static class keyInfo {
        private int id;
        private String key;
    }

    @Data
    @Builder
    static class avatarInfo {
        boolean cross;
        String url;
    }

}
