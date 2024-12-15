package com.yupi.user_center.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.yupi.user_center.model.User;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author MA
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-11-20 16:16:04
*/
public interface UserService extends IService<User> {



    /**
     * 用户注册
     *
     * @param userAccount    用户账号
     * @param userPassword   用户密码
     * @param checkPassword  确认密码
     * @param invitationCode
     * @return 用户id
     */
    long userRegister(String userAccount, String userPassword, String checkPassword, String invitationCode);


    /**
     * 用户登录
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request
     * @return 脱敏后的用户数据
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户数据脱敏
     * @param originalUser 原始用户数据
     * @return 脱敏后用户数据
     */
    User getSafetyUser(User originalUser);

    int userLogOut(HttpServletRequest request);
}
