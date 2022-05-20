package cn.stapxs.blog.service;

import cn.stapxs.blog.model.Comment;

import java.util.List;

public interface CommentService {

    boolean saveComment(Comment comment);
    boolean saveComment(Comment comment, String replyId);

    Comment getComment(String id);
    List<Comment> getCommentList(String artId);

}
