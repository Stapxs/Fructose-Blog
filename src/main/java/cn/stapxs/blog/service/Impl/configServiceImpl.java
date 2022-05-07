package cn.stapxs.blog.service.Impl;

import cn.stapxs.blog.config.JdbcConfig;
import cn.stapxs.blog.mapper.ConfigMapper;
import cn.stapxs.blog.service.ConfigService;
import cn.stapxs.blog.util.Str;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Version: 1.0
 * @Date: 2022/05/01 下午 04:10
 * @ClassName: configServiceImpl
 * @Author: Stapxs
 * @Description 初始化配置相关实现
 **/
@Service
@Transactional
public class configServiceImpl implements ConfigService {

    @Autowired
    ApplicationContext appContext;
    @Autowired
    ConfigMapper config;

    /**
     * @Author Stapxs
     * @Description 获取设置数
     * @Date 下午 04:11 2022/05/01
     * @Param []
     * @return int
    **/
    @Override
    public int getConfigStatue() {
        if(config.getConfigCount() == 0) {
            return 0;
        } else {
            return config.getConfigStatue();
        }
    }

    /**
     * @Author Stapxs
     * @Description 获取数据源状态
     * @Date 下午 05:18 2022/05/01
     * @Param []
     * @return boolean
    **/
    @Override
    public boolean getDataSourceStatue() {
        try {
            DataSource dataSource = (DataSource) appContext.getBean("dataSource");
            return !dataSource.getConnection().isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @Author Stapxs
     * @Description 获取数据库配置
     * @Date 下午 11:44 2022/05/04
     * @Param []
     * @return cn.stapxs.blog.config.JdbcConfig
    **/
    @Override
    public JdbcConfig getDataConfig() {
        try {
            JdbcConfig config = (JdbcConfig) appContext.getBean("jdbcConfig");
            // 去除敏感信息
            Pattern pattern = Pattern.compile(".");
            Matcher matcher = pattern.matcher(config.getPassword());
            String result = matcher.replaceAll("*");
            config.setPassword(result);
            return config;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @Author Stapxs
     * @Description 获取 SQL 版本
     * @Date 下午 02:50 2022/05/05
     * @Param []
     * @return java.lang.String
    **/
    @Override
    public String getSQLVersion() {
        return config.getVersion();
    }

    /**
     * @Author Stapxs
     * @Description 获取系统信息
     * @Date 下午 03:21 2022/05/05
     * @Param []
     * @return java.util.List<java.lang.String>
    **/
    @Override
    public List<String> getSystemInfo() {
        // 获取运行状态
        Properties props = System.getProperties();
        Runtime runtime = Runtime.getRuntime();

        List<String> info = new ArrayList<>();

        info.add(props.getProperty("os.name"));
        info.add(Str.formatByte(runtime.totalMemory() - runtime.freeMemory(),
                false) + " / " + Str.formatByte(runtime.totalMemory(), true));

        return info;
    }

    /**
     * @Author Stapxs
     * @Description 获取用户修改设置的临时 token
     * @Date 下午 04:27 2022/05/05
     * @Param []
     * @return java.lang.String
    **/
    @Override
    public String createToken() {
        // 判断是否存在 token
        if (config.getToken() == null) {
            // 创建 token
            String token = UUID.randomUUID().toString();
            // 存入数据库
            config.setToken(token);
            // 返回 token
            return token;
        }
        return null;
    }

    /**
     * @Author Stapxs
     * @Description 获取 token
     * @Date 下午 06:20 2022/05/05
     * @Param []
     * @return java.lang.String
    **/
    @Override
    public String getToken() {
        return config.getToken();
    }

    /*
     * @Author Stapxs
     * @Description 更新站点设置
     * @Date 下午 08:13 2022/05/05
     * @Param []
     * @return void
    **/
    @Override
    public void updateSiteConfig(SiteConfig siteConfig) {
        config.updateSiteConfig(siteConfig);
    }

    /*
     * @Author Stapxs
     * @Description 获取站点设置
     * @Date 下午 12:20 2022/05/06
     * @Param []
     * @return cn.stapxs.blog.service.Impl.configServiceImpl.SiteConfig
    **/
    @Override
    public SiteConfig getSiteConfig() {
        return config.getSiteConfig();
    }

    // -------------------------------------------------

    @Data
    public static class SiteConfig {
        String fb_name;
        String fb_desc;
        String fb_icon;
        boolean cfg_allow_reg;
        String img_gravatar;
    }
}
