package com.yupi.user_center.service;

import com.yupi.user_center.mapper.UserMapper;
import com.yupi.user_center.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserService userSer;

    @Test
    void testAdd() {
        User user = new User();
        user.setUsername("xiaojiang");
        user.setUserAccount("xiaojiang");
        user.setAvatarUrl("https://th.bing.com/th/id/R.bc2453d14ebe33fe285e263fd3384423?rik=XytpFYxCbOsPGw&riu=http%3a%2f%2fdownhdlogo.yy.com%2fhdlogo%2f640640%2f338%2f338%2f81%2f0638816876%2fu638816876w17jfOQK.png%3f20170118204732&ehk=Q4x7NJ9SqlIpe9ZNLbqNDt2SasQ8CoCy%2fiY8i53OhZ4%3d&risl=&pid=ImgRaw&r=0&sres=1&sresct=1");
        user.setGender(0);
        user.setUserPassword("123456");
        user.setPhone("13735277715");
        user.setEmail("2196716491@qq.com");
        user.setUserStatus(0);
        int insert = userMapper.insert(user);
        System.out.println(user.getId());
    }

    @Test
    void userRegister() {
        long result = userSer.userRegister("", "12345678", "12345678", "");
        Assertions.assertEquals(-1, result);
        result = userSer.userRegister("yupi ", "12345678", "12345678", "");
        Assertions.assertEquals(-1, result);
        result = userSer.userRegister("yu", "12345678", "12345678", "");
        Assertions.assertEquals(-1, result);
        result = userSer.userRegister("yupi", "123456", "123456", "");
        Assertions.assertEquals(-1, result);
        result = userSer.userRegister("yupi", "12345678", "123456789", "");
        Assertions.assertEquals(-1, result);
    }
}