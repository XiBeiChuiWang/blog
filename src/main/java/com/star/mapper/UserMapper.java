package com.star.mapper;

import com.star.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {

    User findByUsernamePassword(@Param("username") String username,@Param("password") String password);

    int updateUser(User user);
}
