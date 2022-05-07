package cn.stapxs.blog.service;

import cn.stapxs.blog.config.JdbcConfig;
import cn.stapxs.blog.service.Impl.configServiceImpl;

import java.util.List;

public interface ConfigService {
    // 系统和数据库信息
    int getConfigStatue();
    boolean getDataSourceStatue();
    JdbcConfig getDataConfig();
    String getSQLVersion();
    List<String> getSystemInfo();
    // 获取 token
    String createToken();
    String getToken();
    // 站点设置
    void updateSiteConfig(configServiceImpl.SiteConfig siteConfig);
    configServiceImpl.SiteConfig getSiteConfig();
}
