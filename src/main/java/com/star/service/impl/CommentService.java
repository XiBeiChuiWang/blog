package com.star.service.impl;

import com.star.domain.Comment;
import com.star.mapper.BlogMapper;
import com.star.mapper.CommentMapper;
import com.star.service.ICommentService;
import com.star.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentService implements ICommentService {
    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    MailUtil mailUtil;

    //存放迭代找出的所有子代的集合
    private List<Comment> tempReplys = new ArrayList<>();

    /**
     * @Description: 查询评论
     * @Auther: ONESTAR
     * @Date: 10:42 2020/6/23
     * @Param: blogId：博客id
     * @Return: 评论消息
     */
    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        //查询出父节点
        List<Comment> comments = commentMapper.findByBlogIdParentIdNull(blogId, Long.parseLong("-1"));
        for(Comment comment : comments){
            Long id = comment.getId();
            String parentNickname1 = comment.getNickname();
            List<Comment> childComments = commentMapper.findByBlogIdParentIdNotNull(blogId,id);
            //查询出子评论
            combineChildren(blogId, childComments, parentNickname1);
            comment.setReplyComments(tempReplys);
            tempReplys = new ArrayList<>();
        }
        return comments;
    }

    /**
     * @Description: 查询出子评论
     * @Auther: ONESTAR
     * @Date: 10:43 2020/6/23
     * @Param: childComments：所有子评论
     * @Param: parentNickname1：父评论姓名
     * @Return:
     */
    private void combineChildren(Long blogId, List<Comment> childComments, String parentNickname1) {
        //判断是否有一级子评论
        if(childComments.size() > 0){
            //循环找出子评论的id
            for(Comment childComment : childComments){
                String parentNickname = childComment.getNickname();
                childComment.setParentNickname(parentNickname1);
                tempReplys.add(childComment);
                Long childId = childComment.getId();
                //查询出子二级评论
                recursively(blogId, childId, parentNickname);
            }
        }
    }

    /**
     * @Description: 循环迭代找出子集回复
     * @Auther: ONESTAR
     * @Date: 10:44 2020/6/23
     * @Param: chileId:子评论id
     * @Param: parentNickname1:子评论姓名
     * @Return:
     */
    private void recursively(Long blogId, Long childId, String parentNickname1) {
        //根据子一级评论的id找到子二级评论
        List<Comment> replayComments = commentMapper.findByBlogIdAndReplayId(blogId,childId);

        if(replayComments.size() > 0){
            for(Comment replayComment : replayComments){
                String parentNickname = replayComment.getNickname();
                replayComment.setParentNickname(parentNickname1);
                Long replayId = replayComment.getId();
                tempReplys.add(replayComment);
                recursively(blogId,replayId,parentNickname);
            }
        }
    }

    //新增评论
    @Override
    public int saveComment(Comment comment) {
        comment.setCreateTime(new Date());
        int comments = commentMapper.saveComment(comment);
        //文章评论计数
        blogMapper.getCommentCountById(comment.getBlogId());

        Long parentCommentId = comment.getParentCommentId();
        Long blogId = comment.getBlogId();
        String msg;
        String mailAds = null;
        if (parentCommentId == -1){
            msg = "有人给你留言了<a href = 'https://star.rainbowsea.top/blog/"+blogId+"'>点击查看</a>";
            mailAds = commentMapper.findMailByBlogId(blogId);
        }else {
            msg = "有人回复了你的留言<a href = 'https://star.rainbowsea.top/blog/"+blogId+"'>点击查看</a>";
            mailAds = commentMapper.findMailByParentId(parentCommentId);
        }

        try {
            mailUtil.sendMail(msg,mailAds);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return comments;
    }

    //删除评论
    @Override
    public void deleteComment(Comment comment,Long id) {
        commentMapper.deleteComment(id);
        blogMapper.getCommentCountById(comment.getBlogId());
    }

    @Override
    public void deleteCommentByBlogId(Long id) {
        commentMapper.deleteCommentByBlogId(id);
    }

    @Override
    public String findMailByBlogId(Long blogId) {
        return commentMapper.findMailByBlogId(blogId);
    }
}
