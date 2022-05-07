package cn.stapxs.blog.service.Impl;

import cn.stapxs.blog.mapper.UserMapper;
import cn.stapxs.blog.model.user.User;
import cn.stapxs.blog.model.user.UserInfo;
import cn.stapxs.blog.service.UserService;
import cn.stapxs.blog.util.RSAEncrypt;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * @Version: 1.0
 * @Date: 2022/02/24 下午 04:48
 * @ClassName: userServiceImpl
 * @Author: Stapxs
 * @Description 用户操作实现
 **/
@Service
@Transactional
public class userServiceImpl implements UserService {

    private static final Logger log = Logger.getLogger(userServiceImpl.class);

    @Autowired
    UserMapper user;

    @Override
    public int getUserCount() {
        return user.getUserCount();
    }

    /**
     * @Author Stapxs
     * @Description 通过用户名获取用户信息
     * @Date 下午 04:49 2022/02/24
     * @Param [name]
     * @return cn.stapxs.blog.model.user.User
    **/
    @Override
    public User getUser(String name) {
        log.info("操作 > 获取用户 > " + name);
        Optional<User> userOptional = Optional.ofNullable(user.getUser(name));
        return userOptional.orElse(null);
    }

    /**
     * @Author Stapxs
     * @Description 通过 id 获取用户信息
     * @Date 下午 01:48 2022/05/01
     * @Param [id]
     * @return cn.stapxs.blog.model.user.User
    **/
    @Override
    public User getUser(int id) {
        log.info("操作 > 获取用户 > " + id);
        Optional<User> userOptional = Optional.ofNullable(user.getUserByID(id));
        return userOptional.orElse(null);
    }

    /**
     * @Author Stapxs
     * @Description 通过邮箱获取用户信息
     * @Date 下午 03:07 2022/05/01
     * @Param [mail]
     * @return cn.stapxs.blog.model.user.User
    **/
    @Override
    public User getUserByMail(String mail) {
        log.info("操作 > 获取用户 > " + mail);
        Optional<User> userOptional = Optional.ofNullable(user.getUserByMail(mail));
        return userOptional.orElse(null);
    }

    /**
     * @Author Stapxs
     * @Description 保存用户
     * @Date 下午 02:56 2022/05/01
     * @Param [user]
    **/
    @Override
    public void saveUser(User user) throws RuntimeException {
        log.info("操作 > 保存用户 > " + user.getUser_mail());
        boolean back = this.user.saveUser(user);
        if(!back) {
            throw new RuntimeException("Mapper: 保存数据库失败！");
        }
    }

    /*
     * @Author Stapxs
     * @Description 获取并保存加密公钥
     * @Date 下午 05:10 2022/02/24
     * @Param [id]
     * @return java.lang.String
    **/
    @Override
    public String getKey(int id) throws NoSuchAlgorithmException, RuntimeException {
        log.info("操作 > 获取保存加密公钥 > " + id);
        // 生成钥匙对
        RSAEncrypt encrypt = new RSAEncrypt();
        encrypt.genKeyPair();
        // 保存私钥
        boolean back = user.setKey(id, encrypt.getKeyMap().get(1));
        if(back) {
            // 返回公钥
            return encrypt.getKeyMap().get(0);
        } else {
            throw new RuntimeException("Mapper: 保存数据库失败！");
        }
    }

    /**
     * @Author Stapxs
     * @Description 删除加密公钥
     * @Date 下午 01:56 2022/05/01
     * @Param [id]
    **/
    @Override
    public void delKey(int id) throws RuntimeException {
        log.info("操作 > 删除加密公钥 > " + id);
        boolean back = user.setKey(id, null);
        if(!back) {
            throw new RuntimeException("Mapper: 保存数据库失败！");
        }
    }

    /**
     * @Author Stapxs
     * @Description 保存 token
     * @Date 下午 02:08 2022/05/01
     * @Param [id, token]
    **/
    @Override
    public void saveToken(int id, String token) throws RuntimeException {
        log.info("操作 > 保存 token > " + id);
        boolean back = user.setToken(id, token);
        if(!back) {
            throw new RuntimeException("Mapper: 保存数据库失败！");
        }
    }

    /**
     * @Author Stapxs
     * @Description 更新上次登录 IP
     * @Date 下午 02:23 2022/05/01
     * @Param [id, ip]
    **/
    @Override
    public void saveLoginIP(int id, String ip) throws RuntimeException {
        log.info("操作 > 更新上次登录 IP > " + id);
        boolean back = user.setLoginIP(id, ip);
        if(!back) {
            throw new RuntimeException("Mapper: 保存数据库失败！");
        }
    }

    /**
     * @Author Stapxs
     * @Description 获取用户信息
     * @Date 上午 11:06 2022/05/07
     * @Param [id]
     * @return cn.stapxs.blog.model.user.UserInfo
    **/
    @Override
    public UserInfo getUserInfo(int id) {
        log.info("操作 > 获取用户信息 > " + id);
        Optional<UserInfo> userInfoOptional = Optional.ofNullable(user.getUserInfoByID(id));
        if(!userInfoOptional.isPresent()) {
            user.newUserInfo(id);
        }
        return userInfoOptional.orElse(new UserInfo(id));
    }
}
