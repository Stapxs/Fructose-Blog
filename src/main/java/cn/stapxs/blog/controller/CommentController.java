package cn.stapxs.blog.controller;

import cn.stapxs.blog.model.Comment;
import cn.stapxs.blog.service.CommentService;
import cn.stapxs.blog.service.UserService;
import cn.stapxs.blog.util.View;
import com.google.gson.Gson;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URLDecoder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * @Version: 1.0
 * @Date: 2022/05/19 上午 10:29
 * @ClassName: CommentController
 * @Author: Stapxs
 * @Description 评论相关操作
 **/
@Controller
public class CommentController {

    private static final Gson gson = new Gson();

    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;

    @PostMapping(value = "api/comment/send", name = "发表评论")
    public String createComment(@RequestBody String str, String aid, Optional<Integer> uid, Optional<String> utoken,
                                Optional<String> info, Optional<String> replay, Model model) {
        try {
            if (uid.isPresent() && utoken.isPresent()) {
                // 验证登录
                if (!userService.verifyLogin(uid.get(), utoken.get())) {
                    return View.api(403, "Forbidden", "验证登录失败！", model);
                }
            }
            // 创建评论对象
            Comment comment = new Comment();
            comment.setCom_id(UUID.randomUUID().toString().replaceAll("-", ""));
            comment.setArt_id(aid);
            if (uid.isPresent() && utoken.isPresent()) {
                comment.setUser_id(uid.get());
            } else {
                comment.setUser_id(-1);
                if (info.isPresent()) {
                    UInfo uInfo = gson.fromJson(URLDecoder.decode(info.get()), UInfo.class);
                    if(uInfo.user_name != null && uInfo.user_mail != null &&
                            !uInfo.user_name.equals("") && !uInfo.user_mail.equals("")) {
                        comment.setUser_name(uInfo.user_name);
                        comment.setUser_mail(uInfo.user_mail);
                        comment.setUser_site(uInfo.user_site);
                    } else {
                        return View.api(400, "Bad Request", "请求参数错误！", model);
                    }
                } else {
                    return View.api(400, "Bad Request", "请求参数错误！", model);
                }
            }
            BInfo body = gson.fromJson(str, BInfo.class);
            if (body.md != null && !body.md.equals("") && body.html != null && !body.html.equals("")) {
                comment.setCom_comment_md(body.md);
                comment.setCom_comment(body.html);
            }
            // 保存数据
            if(replay.isPresent()) {
                commentService.saveComment(comment, replay.get());
            } else {
                commentService.saveComment(comment);
            }
            return View.api(200, "success", "评论成功！", model);
        } catch (Exception e) {
            e.printStackTrace();
            return View.api(500, "Internal Server Error", "服务器错误！", model);
        }
    }

    @GetMapping(value = "api/comment/get/article/{artId}", name = "获取文章评论")
    public String getComments(@PathVariable String artId, Model model) {
        try {
            // 获取评论列表
            List<Comment> commentList = commentService.getCommentList(artId);
            return View.api(200, "success", commentList, model);
        } catch (Exception e) {
            e.printStackTrace();
            return View.api(500, "Internal Server Error", "服务器错误！", model);
        }
    }

    @GetMapping(value = "api/comment/get/com/{comId}", name = "获取评论")
    public String getComment(@PathVariable String comId, Model model) {
        try {
            // 获取评论
            Comment comment = commentService.getComment(comId);
            return View.api(200, "success", comment, model);
        } catch (Exception e) {
            e.printStackTrace();
            return View.api(500, "Internal Server Error", "服务器错误！", model);
        }
    }

    // --------------------------------------------------

    @Data
    public class UInfo {
        private String user_name;
        private String user_mail;
        private String user_site;
    }

    @Data
    public class BInfo {
        private String md;
        private String html;
    }

}
