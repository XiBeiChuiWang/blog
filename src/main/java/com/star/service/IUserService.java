package com.star.service;

import com.star.domain.User;

public interface IUserService {
    User checkUser(String username,String password);
    int updateUser(User user);
}
