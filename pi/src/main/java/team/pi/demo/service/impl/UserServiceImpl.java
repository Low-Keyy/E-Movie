package team.pi.demo.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.pi.demo.mapper.UserMapper;
import team.pi.demo.pojo.User;
import team.pi.demo.service.UserService;
import team.pi.demo.utils.Base64Utils;
import team.pi.demo.utils.Constants;
import team.pi.demo.utils.EmailUtils;
import team.pi.demo.utils.MD5Utils;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

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
    public User login(String username, String password){

        String md5password = MD5Utils.md5(password);

        User user = userMapper.selectUserByname(username);

        if (user!=null && user.getPassword().equals(md5password)){
            return user;
        }
        return null;
    }
}
