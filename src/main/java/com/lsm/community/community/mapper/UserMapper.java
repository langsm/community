package com.lsm.community.community.mapper;

import com.lsm.community.community.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Component
public interface UserMapper {
    @Insert("INSERT INTO USER (NAME,ACCOUNT_ID,TOKEN,GMT_CREATE,GMT_MODIFIED) VALUES (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);
}
