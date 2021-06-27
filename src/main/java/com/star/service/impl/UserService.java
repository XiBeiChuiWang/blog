package com.star.service.impl;

import com.star.domain.User;
import com.star.mapper.UserMapper;
import com.star.service.IUserService;
import com.star.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User checkUser(String username,String password) {
        return userMapper.findByUsernamePassword(username, MD5Utils.code(password));
    }

    @Override
    public int updateUser(User user) {
        user.setPassword(MD5Utils.code(user.getPassword()));
        return userMapper.updateUser(user);
    }
}
