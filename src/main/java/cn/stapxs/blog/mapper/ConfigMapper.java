package cn.stapxs.blog.mapper;

import cn.stapxs.blog.model.Config;
import cn.stapxs.blog.service.Impl.configServiceImpl;
import org.apache.ibatis.annotations.*;
import org.springframework.web.bind.annotation.PathVariable;

@Mapper
public interface ConfigMapper {

    @Select("select * from fb_config")
    Config getConfig();
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
    @Select("select ${name} from fb_config")
    String getStringConfig(String name);

    @Insert("insert into fb_config(cfg_token) values(#{token})")
    void setToken(String token);

    @Update("update fb_config set fb_name=#{fb_name}, fb_desc=#{fb_desc}, fb_icon=#{fb_icon}, cfg_allow_reg=#{cfg_allow_reg}")
    void updateSiteConfig(configServiceImpl.SiteConfig config);
    @Update("update fb_config set ${name}=#{value}")
    void updateStringConfig(@Param("name") String name, @Param("value") String value);
}
