package com.yupi.user_center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.user_center.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}