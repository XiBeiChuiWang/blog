package com.star.service.impl;

import com.star.domain.Message;
import com.star.mapper.MessageMapper;
import com.star.service.MessageService;
import com.star.util.MailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Description: 留言业务层接口实现类
 * @Date: Created in 11:45 2020/4/16
 * @Author: ONESTAR
 * @QQ群: 530311074
 * @URL: https://onestar.newstar.net.cn/
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageMapper messageMapper;

    @Autowired
    MailUtil mailUtil;

    //存放迭代找出的所有子代的集合
    private List<Message> tempReplys = new ArrayList<>();

    /**
     * @Description: 查询留言
     * @Auther: ONESTAR
     * @Date: 17:26 2020/4/14
     * @Param:
     * @Return: 留言消息
     */
    @Override
    public List<Message> listMessage() {
        //查询出父节点
        List<Message> messages = messageMapper.findByParentIdNull(Long.parseLong("-1"));
        for(Message message : messages){
            Long id = message.getId();
            String parentNickname1 = message.getNickname();
            List<Message> childMessages = messageMapper.findByParentIdNotNull(id);
            //查询出子留言
            combineChildren(childMessages, parentNickname1);
            message.setReplyMessages(tempReplys);
            tempReplys = new ArrayList<>();
        }
        return messages;
    }

    /**
     * @Description: 查询出子留言
     * @Auther: ONESTAR
     * @Date: 17:31 2020/4/14
     * @Param: childMessages：所有子留言
     * @Param: parentNickname1：父留言的姓名
     * @Return:
     */
    private void combineChildren(List<Message> childMessages, String parentNickname1) {
        //判断是否有一级子回复
        if(childMessages.size() > 0){
            //循环找出子留言的id
            for(Message childMessage : childMessages){
                String parentNickname = childMessage.getNickname();
                childMessage.setParentNickname(parentNickname1);
                tempReplys.add(childMessage);
                Long childId = childMessage.getId();
                //查询二级以及所有子集回复
                recursively(childId, parentNickname);
            }
        }
    }

    /**
     * @Description: 循环迭代找出子集回复
     * @Auther: ONESTAR
     * @Date: 17:33 2020/4/14
     * @Param: childId：子留言的id
     * @Param: parentNickname1：子留言的姓名
     * @Return:
     */
    private void recursively(Long childId, String parentNickname1) {
        //根据子一级留言的id找到子二级留言
        List<Message> replayMessages = messageMapper.findByReplayId(childId);

        if(replayMessages.size() > 0){
            for(Message replayMessage : replayMessages){
                String parentNickname = replayMessage.getNickname();
                replayMessage.setParentNickname(parentNickname1);
                Long replayId = replayMessage.getId();
                tempReplys.add(replayMessage);
                //循环迭代找出子集回复
                recursively(replayId,parentNickname);
            }
        }
    }

    @Override
    //存储留言信息
    public int saveMessage(Message message) {
        message.setCreateTime(new Date());
        String msg;
        String mailAds = null;
        if (message.getParentMessageId() == -1){
            msg = "有人给你留言了，<a href='https://star.rainbowsea.top/message'>点击查看</a>";
        }else {
            msg = "有人回复了你的留言，<a href='https://star.rainbowsea.top/message'>点击查看</a>";
            mailAds = messageMapper.findEmailByParentId(message.getParentMessageId());
        }
        try {
            mailUtil.sendMail(msg,mailAds);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        return messageMapper.saveMessage(message);
    }

//    删除留言
    @Override
    public void deleteMessage(Long id) {
        messageMapper.deleteMessage(id);
    }
}