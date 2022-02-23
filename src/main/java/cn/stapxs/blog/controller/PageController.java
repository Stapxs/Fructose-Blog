package cn.stapxs.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Version: 1.0
 * @Date: 2022/02/22 下午 04:17
 * @ClassName: PageController
 * @Author: Stapxs
 * @Description TO DO
 **/
@Controller
public class PageController {
    @RequestMapping("/")
    public String index() { return "index"; }
}
