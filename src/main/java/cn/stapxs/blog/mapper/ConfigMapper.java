package cn.stapxs.blog.mapper;

import cn.stapxs.blog.service.Impl.configServiceImpl;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ConfigMapper {

    @Select("select count(*) from fb_config")
    int getConfigCount();
    @Select("select cfg_statue from fb_config")
    int getConfigStatue();
    @Select("select version()")
    String getVersion();
    @Select("select cfg_token from fb_config")
    String getToken();
    @Select("SELECT * FROM fb_config")
    configServiceImpl.SiteConfig getSiteConfig();

    @Insert("insert into fb_config(cfg_token) values(#{token})")
    void setToken(String token);

    @Update("update fb_config set fb_name=#{fb_name}, fb_desc=#{fb_desc}, fb_icon=#{fb_icon}, cfg_allow_reg=#{cfg_allow_reg}")
    void updateSiteConfig(configServiceImpl.SiteConfig config);
}
