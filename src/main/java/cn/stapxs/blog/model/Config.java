package cn.stapxs.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Blob;

/**
 * @Version: 1.0
 * @Date: 2022/05/12 下午 12:32
 * @ClassName: Config
 * @Author: Stapxs
 * @Description 站点设置结构
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Config {
    private String fb_name;
    private String fb_desc;
    private Blob fb_icon;
    private String img_gravatar;
    private boolean cfg_allow_reg;
    private String cfg_theme;
    private String cfg_token;
    private int cfg_statue;
}
