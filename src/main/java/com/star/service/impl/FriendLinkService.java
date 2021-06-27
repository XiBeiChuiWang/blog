package com.star.service.impl;

import com.star.domain.FriendLink;
import com.star.mapper.FriendLinkMapper;
import com.star.service.IFriendLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendLinkService implements IFriendLinkService {

    @Autowired
    FriendLinkMapper friendLinkMapper;

    @Override
    public List<FriendLink> listFriendLink() {
        return friendLinkMapper.listFriendLink();
    }

    @Override
    public int saveFriendLink(FriendLink friendLink) {
        return friendLinkMapper.saveFriendLink(friendLink);
    }

    @Override
    public FriendLink getFriendLinkByBlogaddress(String blogaddress) {
        return friendLinkMapper.getFriendLinkByBlogaddress(blogaddress);
    }

    @Override
    public FriendLink getFriendLink(Long id) {
        return friendLinkMapper.getFriendLink(id);
    }

    @Override
    public int updateFriendLink(FriendLink friendLink) {
        return friendLinkMapper.updateFriendLink(friendLink);
    }

    @Override
    public void deleteFriendLink(Long id) {
        friendLinkMapper.deleteFriendLink(id);
    }
}
