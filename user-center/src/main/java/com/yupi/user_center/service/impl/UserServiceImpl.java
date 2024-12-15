package com.yupi.user_center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yupi.user_center.mapper.UserMapper;
import com.yupi.user_center.model.User;
import com.yupi.user_center.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import static com.yupi.user_center.contant.Usercontant.INVITATION_CODE;
import static com.yupi.user_center.contant.Usercontant.USER_LOGIN_STATE;

/**
 * @author MA
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-11-20 16:16:04
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "xiao_jiang";



    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword, String invitationCode) {
        // 1.校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, invitationCode)) {
            return -1;
        }
        if (userAccount.length() < 4) {
            return -1;
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }

        // 校验账户不能包含特殊字符
        if (!userAccount.matches("[a-zA-Z0-9_]*")) {
            return -2;
        }
        // 密码和确认密码相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        // 确认邀请码
        if (!invitationCode.equals(INVITATION_CODE)) {
            return -3;
        }
        // 用户不能重复 （涉及数据库操作可以放在最后以提高性能）
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUserAccount, userAccount);
        if (this.count(queryWrapper) > 0) {
            return -1;
        }
        // 密码加密

        String resultPasswrod = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        // 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(resultPasswrod);
        user.setInvitationCode(invitationCode);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            return -1;
        }
        return user.getId();
    }

    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        if (userAccount.length() < 4 || userPassword.length() < 8) {
            return null;
        }
        if (!userAccount.matches("[a-zA-Z0-9_]*")) {
            return null;
        }
        // 查询用户是否存在
        // 密码校验
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getUserAccount, userAccount);
        userLambdaQueryWrapper.eq(User::getUserPassword, encryptPassword);
        User user = this.getOne(userLambdaQueryWrapper);
        if (user == null) {
            log.info("user login failed, userAccount can't match userPassword");
            return null;
        }
        // 脱敏
        User safetyUser = getSafetyUser(user);
        // 记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    @Override
    public User getSafetyUser(User originalUser) {
        if (originalUser == null) {
            return null;
        }
        User safetyUser = new User();
        safetyUser.setId(originalUser.getId());
        safetyUser.setUsername(originalUser.getUsername());
        safetyUser.setUserAccount(originalUser.getUserAccount());
        safetyUser.setAvatarUrl(originalUser.getAvatarUrl());
        safetyUser.setGender(originalUser.getGender());
        safetyUser.setPhone(originalUser.getPhone());
        safetyUser.setEmail(originalUser.getEmail());
        safetyUser.setUserStatus(originalUser.getUserStatus());
        safetyUser.setCreateTime(originalUser.getCreateTime());
        safetyUser.setUserRole(originalUser.getUserRole());
        safetyUser.setInvitationCode(originalUser.getInvitationCode());
        return safetyUser;
    }

    @Override
    public int userLogOut(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 0;
    }
}




