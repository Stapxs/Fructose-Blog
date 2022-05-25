package cn.stapxs.blog.service;

import cn.stapxs.blog.model.user.User;
import cn.stapxs.blog.model.user.UserInfo;

import java.security.NoSuchAlgorithmException;

public interface UserService {
    int getUserCount();

    // 登陆系统
    User getUser(String name);
    User getUser(int id);
    User getUserByMail(String mail);

    void saveUser(User user) throws RuntimeException;

    String getKey(int id) throws NoSuchAlgorithmException, RuntimeException;
    void delKey(int id) throws RuntimeException;
    void saveToken(int id, String token) throws RuntimeException;
    void saveLoginIP(int id, String ip) throws RuntimeException;
    void updateUser(int id, String name, String value);

    boolean verifyLogin(int id, String token);
    boolean verifyAdministrator(int id, String token);

    void logout(int id);

    // 用户信息系统
    UserInfo getUserInfo(int id);
    void updateUserInfo(int id, String name, String value);

    boolean verifyEdit(int id);
}
