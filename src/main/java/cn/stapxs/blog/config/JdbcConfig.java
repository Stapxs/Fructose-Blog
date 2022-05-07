package cn.stapxs.blog.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Version: 1.0
 * @Date: 2021/9/22 下午 4:44
 * @ClassName: JdbcConfig
 * @Author: Stapxs
 * @Description TO DO
 **/
@Component
@PropertySource("classpath:jdbc.properties")
@Data
public class JdbcConfig {
    @Value("${databaseType}")
    private String type;
    @Value("${jdbc.driverClassName}")
    private String driver;
    @Value("${jdbc.url}")
    private String url;
    @Value("${jdbc.username}")
    private String name;
    @Value("${jdbc.password}")
    private String password;
}
