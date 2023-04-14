package team.pi.demo.service;

/*
* 用户模块的业务逻辑接口类
* */


import team.pi.demo.pojo.RespBean;
import team.pi.demo.pojo.User;

public interface UserService {

    //检测用户名是否存在
    boolean checkedUser(String username);
    //注册业务逻辑
    int registerUser(User user);

    //激活方法 return 0失败 1成功 2已经激活
    int activeUser(String code,String username);

    RespBean login(User user);
    public RespBean refreshToken(String token);

    RespBean updateUserInfo(User user);
}
