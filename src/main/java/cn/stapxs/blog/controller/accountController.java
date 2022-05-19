package cn.stapxs.blog.controller;

import cn.stapxs.blog.model.Back;
import cn.stapxs.blog.model.Config;
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
import org.springframework.web.bind.annotation.RequestBody;

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
    // 安全服务相关

    @GetMapping(value = "api/account/key/{name}", name = "获取加密公钥")
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
    public String loginAccount(int id, String str, Optional<Boolean> nd, HttpServletRequest request, Model model) {
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
                if(!nd.isPresent()) {
                    // 删除私钥
                    userService.delKey(user.get().getUser_id());
                }
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

    @PostMapping(value = "api/account/verify", name = "验证登录")
    public String verifyLogin(int id, String str, Model model) {
        try {
            if (userService.verifyLogin(id, str)) {
                return View.api(200, "success", "登陆状态有效！", model);
            } else {
                return View.api(403, "Forbidden", "登陆状态无效！", model);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return View.api(500, "Internal Server Error", ex.getMessage(), model);
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

    @PostMapping(value = "api/account/password", name = "修改密码")
    public String changePassword(int id, String str, Model model) {
        str = str.replace(" ", "+");
        Optional<User> user = Optional.ofNullable(userService.getUser(id));
        if(user.isPresent()) {
            // 解码密码
            try {
                RSAEncrypt encrypt = new RSAEncrypt();
                str = encrypt.decrypt(str, user.get().getLogin_key());
                // 加密密码
                PBKDF2 pbkdf2 = new PBKDF2(32, 64, 30000);
                str = pbkdf2.getSaveStr(str);
                // 修改密码
                userService.updateUser(user.get().getUser_id(), "user_password", str);
                // 删除 token
                userService.updateUser(user.get().getUser_id(), "user_token", null);
                // 删除私钥
                userService.delKey(user.get().getUser_id());
                // 返回
                return View.api(200, "success", "修改成功", model);
            } catch (BadPaddingException be) {
                be.printStackTrace();
                // RSA 解密失败
                return View.api(400, "Bad Request", "解密失败", model);
            } catch (NullPointerException ne) {
                ne.printStackTrace();
                // 未申请公钥
                return View.api(403, "Forbidden", "无效的操作", model);
            } catch (Exception ex) {
                ex.printStackTrace();
                // 其他错误
                return View.api(500, "Internal Server Error", ex.getMessage(), model);
            } finally {
                // 删除私钥
                userService.delKey(user.get().getUser_id());
            }
        } else {
            // 删除私钥
            userService.delKey(user.get().getUser_id());
            // 用户不存在
            return View.api(404, "Not Found", "用户不存在", model);
        }
    }

    // -------------------------------------------------
    // 用户信息相关
    @GetMapping(value = {"api/account/avatar/{id}", "api/account/avatar/{id}/{type}"}, name = "获取用户头像链接")
    public String getUserAvatar(@PathVariable int id, @PathVariable Optional<String> type, HttpServletResponse response, Model model) throws IOException {
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
                        Config siteConfig = configService.getConfig();
                        String gravatarUrl = "https://www.gravatar.com/avatar/";
                        if (siteConfig.getImg_gravatar() != null) {
                            gravatarUrl = siteConfig.getImg_gravatar();
                        }
                        gravatarUrl = gravatarUrl + DigestUtils.md5Hex(mail.toLowerCase()) + "?s=200";
                        if(type.isPresent() && type.get().equals("url")){
                            model.addAttribute("code", "200");
                            model.addAttribute("str", gravatarUrl);
                            return "api";
                        } else if(type.isPresent() && type.get().equals("img")) {
                            response.sendRedirect(gravatarUrl);
                            return null;
                        }
                        else {
                            return View.api(200, "success",
                                    new avatarInfo(false, gravatarUrl)
                                    , model);
                        }
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

    @PostMapping(value = "api/account/config/{id}", name = "获取用户设置档")
    public String getUserConfig(@PathVariable int id, String token, Model model) {
        // 验证登录
        if(userService.verifyLogin(id, token)) {
            // 获取用户设置档
            UserInfo userInfo = userService.getUserInfo(id);
            return View.api(200, "success", userInfo.getUser_config(), model);
        }
        return View.api(403, "Forbidden", "验证登录失效！", model);
    }

    @GetMapping(value = "api/account/base/{id}", name = "获取用户基本信息")
    public String getUserBase(@PathVariable int id, Model model) {
        Optional<User> user = Optional.ofNullable(userService.getUser(id));
        Optional<UserInfo> userInfo = Optional.ofNullable(userService.getUserInfo(id));
        if(user.isPresent() && userInfo.isPresent()) {
            UserFullInfo userfullInfo = new UserFullInfo();
            userfullInfo.setUser_id(user.get().getUser_id());
            userfullInfo.setUser_name(user.get().getUser_name());
            userfullInfo.setUser_nick(userInfo.get().getUser_nick());
            userfullInfo.setUser_link(userInfo.get().getUser_link());
            return View.api(200, "success", userfullInfo, model);
        }
        return View.api(404, "not found", "获取用户信息失败！", model);
    }

    @PostMapping(value = "api/account/info/{id}", name = "获取用户完整信息")
    public String getUserInfo(@PathVariable int id, String token, Model model) {
        // 验证登录
        System.out.println(id + " / " + token);
        if(userService.verifyLogin(id, token)) {
            User user = userService.getUser(id);
            UserInfo userInfo = userService.getUserInfo(id);
            UserFullInfo userfullInfo = new UserFullInfo();
            // 填充
            userfullInfo.user_id = user.getUser_id();
            userfullInfo.user_name = user.getUser_name();
            userfullInfo.user_mail = user.getUser_mail();
            userfullInfo.user_nick = userInfo.getUser_nick();
            userfullInfo.user_link = userInfo.getUser_link();

            userfullInfo.login_ip = user.getLogin_ip();
            userfullInfo.reg_ip = user.getReg_ip();
            // 返回
            return View.api(200, "success", userfullInfo, model);
        }
        return View.api(403, "Forbidden", "验证登录失效！", model);
    }

    @PostMapping(value = "api/account/info/set/{name}/{id}", name = "设置用户信息")
    public String setUserInfo(@PathVariable String name, @PathVariable int id, String token, String value, Model model) {
        // 验证登录
        if(userService.verifyLogin(id, token)) {
            String[] names = name.split("-");
            if(names[0].equals("user")) {
                userService.updateUser(id, names[1], value);
            } else {
                userService.updateUserInfo(id, names[1], value);
            }
            return View.api(200, "success", "设置成功！", model);
        } else {
            return View.api(403, "Forbidden", "验证登录失效！", model);
        }
    }

    @Data
    static class UserFullInfo {
        private int user_id;
        private String user_name;
        private String user_mail;
        private String user_nick;
        private String user_link;

        private String login_ip;
        private String reg_ip;
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
