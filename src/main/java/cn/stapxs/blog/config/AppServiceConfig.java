package cn.stapxs.blog.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.*;

/**
 * @Version: 1.0
 * @Date: 2021/9/1 下午 4:28
 * @ClassName: AppServiceConfig
 * @Author: Stapxs
 * @Description TO DO
 **/
@EnableWebMvc
@Configuration
@ComponentScan("cn.stapxs.blog")
public class AppServiceConfig implements WebMvcConfigurer {
    //配置jsp视图解析器
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/web/",".jsp");
    }

    //配置静态资源处理
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("/web/js/");
        registry.addResourceHandler("/css/**").addResourceLocations("/web/css/");
        registry.addResourceHandler("/images/**").addResourceLocations("/web/images/");
        registry.addResourceHandler("/editor/**").addResourceLocations("/web/editor/");
        registry.addResourceHandler("/theme/**").addResourceLocations("/web/theme/");
    }
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}
