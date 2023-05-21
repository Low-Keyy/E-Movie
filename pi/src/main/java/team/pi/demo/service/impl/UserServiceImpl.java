package team.pi.demo.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import team.pi.demo.mapper.UserMapper;
import team.pi.demo.mapper.UserMovieCollectMapper;
import team.pi.demo.mapper.UserMoviePlayMapper;
import team.pi.demo.pojo.*;
import team.pi.demo.service.UserService;
import team.pi.demo.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static team.pi.demo.utils.Constants.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private  RedisCache redisCache;

    @Autowired
    private UserMovieCollectMapper userMovieCollectMapper;
    @Autowired
    private UserMoviePlayMapper userMoviePlayMapper;
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
        //头像复制
        String filePath=projectPath+File.separator+"img"+File.separator;
        File def = new File(filePath+"default.png");
        File des = new File(filePath+user.getAvartar());
        if(!des.getParentFile().exists())
            des.getParentFile().mkdirs();
        if (des.exists())
            des.delete();
        try {
            FileCopyUtils.copy(def,des);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        //如果认证通过了，使用userid生成jwt jwt存入RespBean返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        Map<String, String> map = generateToken(loginUser);
        map.put("userName",loginUser.getUser().getUserName());
        map.put("nikeName",loginUser.getUser().getNickName());
        map.put("avartar",loginUser.getUser().getAvartar());
        map.put("role",loginUser.getUser().getRole());
        return new RespBean(RESP_SUCCESS,"登录成功",map);
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

    @Override
    public RespBean insertPlayRecord(UserMoviePlay userMoviePlay) {
        int id=userMapper.selectUserByname(userMoviePlay.getUserName()).getId();
        userMoviePlay.setUser_id(id);
        int count = userMoviePlayMapper.insert(userMoviePlay);
        if(count > 0){
            return new RespBean(RESP_SUCCESS,"用户成功存储播放历史",null);
        }else{
            return  new RespBean(NOT_IMPLEMENTED,"用户失败存储播放历史",null);
        }
    }

    @Override
    public RespBean insertCollectRecord(UserMovieCollect userMovieCollect) {
        int id=userMapper.selectUserByname(userMovieCollect.getUserName()).getId();
        userMovieCollect.setUser_id(id);
        int count = userMovieCollectMapper.insert(userMovieCollect);
        if(count > 0){
            return new RespBean(RESP_SUCCESS,"用户成功收藏",null);
        }else{
            return  new RespBean(NOT_IMPLEMENTED,"用户失败收藏",null);
        }
    }

    @Override
    public RespBean verify(String userName) {
        User user=userMapper.selectUserByname(userName);
        if(Objects.isNull(user))
        {
            return RespBean.error("用户名不存在");
        }

        if (user.getDeleted()!=0){
            throw new RuntimeException("账号不存在");
        }
        String email = user.getEmail();
        int encode = new Random().nextInt(9000) + 1000;
        //存入redis
        redisCache.setCacheObject("verify"+userName,encode);
        redisCache.expire("verify"+userName,TOKEN_VALIDITY);
        EmailUtils.createCode(userName,user.getEmail(),encode);
        return RespBean.success("验证码已发送");
    }

    @Override
    public RespBean changepsw(VerifyUser user) {
        //判断验证码是否正确
        int encode = redisCache.getCacheObject("verify" + user.getUserName());
        if(Objects.isNull(encode)){
            return RespBean.error("验证码过期");
        }
        if(user.getVerifyCode() != encode){
            return RespBean.error("验证码错误");
        }
        User temp = new User();
        temp.setUserName(user.getUserName());
        temp.setPassword(user.getPassword());
        //redis删除
        redisCache.deleteObject("verify" + user.getUserName());
        int i = userMapper.updateUser(temp);
        if(i!=0) return RespBean.success("修改成功");
        else return RespBean.error("修改失败,请重新尝试");
    }

    @Override
    public RespBean changeAvartars(MultipartFile file, LoginUser loginUser) {
        final String projectPath = System.getProperty("user.dir");
        String filePath=projectPath+File.separator+"img"+File.separator;
        String fileName = loginUser.getUser().getUserName()+".png";
        File filedest = new File(filePath + fileName);
        if(!filedest.getParentFile().exists())
            filedest.getParentFile().mkdirs();
        if (filedest.exists())
            filedest.delete();
        try {
            file.transferTo(filedest);
            User temp = new User();
            temp.setUserName(loginUser.getUser().getUserName());
            temp.setAvartar(fileName);
            userMapper.updateUser(temp);
            loginUser.getUser().setAvartar(fileName);

            return RespBean.success("用户头像更新成功");
        } catch (IOException e) {
            e.printStackTrace();
            return RespBean.error("用户头像更新失败，未知原因");
        }
    }

    @Override
    public RespBean userInfo(LoginUser loginUser) {
        User user = userMapper.selectUserByname(loginUser.getUser().getUserName());
        user.setPassword("");
        return RespBean.success("查询成功",user);
    }

    @Override
    public RespBean logout(LoginUser loginUser, String token) {
        redisCache.deleteObject(token);
        return RespBean.success("注销成功");
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
//        String refreshJwt = JwtUtil.createJWT(userId);

        //把完整的用户信息存入redis  accessJwt、refreshJwt作为key 并设置过期时间。
        // refreshJwt过期时间为accessToken过期时间的两倍
        redisCache.setCacheObject(accessJwt,loginUser);
        boolean expire = redisCache.expire(accessJwt, TOKEN_VALIDITY*10);//50分钟
//        redisCache.setCacheObject(refreshJwt,loginUser);
//        redisCache.expire(refreshJwt,TOKEN_VALIDITY*2);

        //封装进map中
        Map<String,String> map = new HashMap<>();
        map.put("token",accessJwt);
//        map.put("refreshToken",refreshJwt);

        return map;
    }
}
