package cn.stapxs.blog.service.Impl;

import cn.stapxs.blog.mapper.ArticleMapper;
import cn.stapxs.blog.mapper.CommentMapper;
import cn.stapxs.blog.model.Article;
import cn.stapxs.blog.model.Comment;
import cn.stapxs.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Version: 1.0
 * @Date: 2022/05/19 下午 02:56
 * @ClassName: commentServiceImpl
 * @Author: Stapxs
 * @Description TO DO
 **/
@Service
public class commentServiceImpl implements CommentService {

    @Autowired
    CommentMapper commentMapper;
    @Autowired
    ArticleMapper articleMapper;

    /**
     * @Author Stapxs
     * @Description 保存评论
     * @Date 下午 02:56 2022/05/19
     * @Param [comment]
     * @return void
    **/
    @Override
    public boolean saveComment(Comment comment) {
        // 获取文章
        Optional<Article> article = Optional.ofNullable(articleMapper.getArticleById(comment.getArt_id()));
        if(article.isPresent() || comment.getArt_id() == null) {
            commentMapper.saveComment(comment);
            return true;
        } else {
            return false;
        }
    }
    @Override
    public boolean saveComment(Comment comment, String replyId) {
        comment.setArt_id(null);
        boolean back = saveComment(comment);
        if(back) {
            // 保存回复
            Comment reply = getComment(replyId);
            if (reply.getCom_children() == null) {
                reply.setCom_children(comment.getCom_id());
            } else {
                reply.setCom_children(reply.getCom_children() + "," + comment.getCom_id());
            }
            updateCommentChildren(reply);
            return true;
        }
        return false;
    }

    /**
     * @Author Stapxs
     * @Description 获取评论
     * @Date 下午 02:21 2022/05/20
     * @Param [id]
     * @return cn.stapxs.blog.model.Comment
    **/
    @Override
    public Comment getComment(String id) {
        return commentMapper.getCommentById(id);
    }

    /**
     * @Author Stapxs
     * @Description 获取文章的一级评论列表
     * @Date 上午 10:34 2022/05/20
     * @Param [artId]
     * @return java.util.List<cn.stapxs.blog.model.Comment>
    **/
    @Override
    public List<Comment> getCommentList(String artId) {
        return commentMapper.getArticleCommentList(artId);
    }

    // -------------------------------------------------

    public void updateCommentChildren(Comment comment) {
        commentMapper.updateCommentChildren(comment);
    }

}
