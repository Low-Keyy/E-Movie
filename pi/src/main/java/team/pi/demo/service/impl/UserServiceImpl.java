package team.pi.demo.service.impl;


import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import team.pi.demo.mapper.UserMapper;
import team.pi.demo.pojo.LoginUser;
import team.pi.demo.pojo.RespBean;
import team.pi.demo.pojo.User;
import team.pi.demo.service.UserService;
import team.pi.demo.utils.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static team.pi.demo.utils.Constants.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private  RedisCache redisCache;
    @Override
    public boolean checkedUser(String username) {
        User user=userMapper.selectUserByname(username);
        if (user != null) {
            return true;
        }
        return false;
    }

    @Override
    public int registerUser(User user) {

        int i = userMapper.insertUser(user);

        EmailUtils.sendEmail(user);

        return i;
    }

    @Override
    public int activeUser(String code,String username) {

        User user = userMapper.selectUserByCode(code);
        if (user == null || !(user.getUserName().equals(username))){
            return Constants.ACTIVE_FAIL;
        }

        if (user.getActive().equals(Constants.USER_ACTIVE)) {
            return Constants.ACTIVE_ALREADY;
        }

        int i = userMapper.updateStatusByUserName(user.getUserName());

        if (i > 0) {
            return Constants.ACTIVE_SUCCESS;
        }

        return Constants.ACTIVE_FAIL;
    }

    @Override
    public RespBean login(User user){

        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //如果认证没通过，给出对应的提示
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登录失败");
        }

        //如果认证通过了，使用userid生成两个jwt jwt存入RespBean返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Map<String, String> map = generateToken(loginUser);
        return new RespBean(VERIFY_TRUE,"登录成功",map);
    }


    public RespBean refreshToken(String token){

        //通过refreshToken从redis中获取用户信息
        LoginUser loginUser = redisCache.getCacheObject(token);

        //删除原有refreshToken
        redisCache.deleteObject(token);

        //封装进map中
        Map<String,String> map = generateToken(loginUser);
        return new RespBean(UPDATE_TOKEN_SUCCESS,"更新token成功",map);
    }

    @Override
    public RespBean updateUserInfo(User user) {
        int i = userMapper.updateUser(user);
        if(i>0){
            return  new RespBean(RESP_SUCCESS,"更新用户成功",user);
        }else {
            return  new RespBean(NOT_IMPLEMENTED,"更新用户失败",user);
        }
    }

    /**
     * 生成token并存入Redis中
     * @param loginUser
     * @return
     */
    public Map<String,String> generateToken(LoginUser loginUser){
        //生成accessToken和refreshToken，并存入Redis
        String userId = String.valueOf(loginUser.getUser().getId());
        String accessJwt = JwtUtil.createJWT(userId);
        String refreshJwt = JwtUtil.createJWT(userId);

        //把完整的用户信息存入redis  accessJwt、refreshJwt作为key 并设置过期时间。
        // refreshJwt过期时间为accessToken过期时间的两倍
        redisCache.setCacheObject(accessJwt,loginUser);
        redisCache.expire(accessJwt,TOKEN_VALIDITY);
        redisCache.setCacheObject(refreshJwt,loginUser);
        redisCache.expire(refreshJwt,TOKEN_VALIDITY*2);

        //封装进map中
        Map<String,String> map = new HashMap<>();
        map.put("accessToken",accessJwt);
        map.put("refreshToken",refreshJwt);

        return map;
    }
}
