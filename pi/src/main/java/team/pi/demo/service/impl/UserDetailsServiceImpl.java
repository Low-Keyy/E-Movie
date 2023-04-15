package team.pi.demo.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import team.pi.demo.mapper.UserMapper;
import team.pi.demo.pojo.LoginUser;
import team.pi.demo.pojo.User;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.selectUserByname(username);
        //如果没有查询到用户就抛出异常
        if(Objects.isNull(user)){
            throw new RuntimeException("用户名或者密码错误");
        }

        //把数据封装成UserDetails返回
        return new LoginUser(user);
    }


//    @Autowired
//    private MenuMapper menuMapper;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//
//        //查询用户信息
//        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(User::getUserName,username);
//        User user = userMapper.selectOne(queryWrapper);
//        //如果没有查询到用户就抛出异常
//        if(Objects.isNull(user)){
//            throw new RuntimeException("用户名或者密码错误");
//        }
//
////        List<String> list = new ArrayList<>(Arrays.asList("test","admin"));
//        List<String> list = menuMapper.selectPermsByUserId(user.getId());
//        //把数据封装成UserDetails返回
//        return new LoginUser(user,list);
//    }


}
