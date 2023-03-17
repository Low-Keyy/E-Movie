package team.pi.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.pi.demo.pojo.RespBean;
import team.pi.demo.pojo.User;
import team.pi.demo.service.UserService;
import team.pi.demo.utils.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.server.PathParam;

@Controller
@Api(value="用户controller",tags={"用户操作接口"})
public class UserController {

    @Autowired
    private UserService userService;
    @ResponseBody
    @ApiOperation(value="用户注册",notes="输入用户名，昵称,密码，确认密码，邮箱地址,网址格式为 /user/register/{name}/{password1}/{password2}/{mail}")
    @PostMapping("/user/register/{name}/{nickname}/{password1}/{password2}/{mail}")
    public RespBean register(@PathVariable("name") String name, @PathVariable("nickname") String nickname,@PathVariable("password1") String password1,
                             @PathVariable("password2") String password2, @PathVariable("mail") String mail){

        if(name==null || password1==null || password2==null || mail==null){
            return RespBean.error("输入框中存在空值");
        }
        if (!password1.equals(password2)){
            return RespBean.error("密码不一致");
        }
        if (name.length() <6 || name.length() > 16){
            return RespBean.error("用户名的长度为6-16个字符");
        }
        if (nickname.length() <=2 || nickname.length() > 16){
            return RespBean.error("昵称的长度为2-16个字符");
        }
        if (password1.length()<6 || password1.length()>24){
            return RespBean.error("密码长度位6-24个字符");
        }
        if (!EmailCheckUtils.isEmail(mail)){
            return RespBean.error("邮箱格式错误");
        }
        boolean b = userService.checkedUser(name);
        if (b){
            //用户存在
            return RespBean.error("用户名已存在");
        }

        User user = new User();
        user.setEmail(mail);
        user.setUserName(name);
        user.setNickName(nickname);
        user.setActive(Constants.USER_NOT_ACTIVE);//默认未被激活
        user.setCode(RandomUtils.createActive());//邮箱激活代码
        user.setPassword(MD5Utils.md5(password1));//处理密码为MD5格式存储
        //TODO
        //用户头像设为默认地址

        int i =userService.registerUser(user);
        if (i==0){
            return RespBean.success("注册失败，请检查网络连接");
        }
        return RespBean.success("注册成功，请激活账户");
    }
    @ResponseBody
    @ApiOperation(value="激活账户",notes="无需输入，均由系统自动生成，网址格式为 /user/active/{用户名}/{激活码}")
    @GetMapping("/user/active/{username}/{c}")
    public RespBean active(@ApiParam("用户名") @PathVariable("username") String username,@ApiParam("激活码") @PathVariable("c") String c){
        String code = Base64Utils.decode(c);
        int i = userService.activeUser(code,username);
        if (i ==Constants.ACTIVE_FAIL) {
            return RespBean.success("激活失败，请重新激活");
        }else if (i== Constants.ACTIVE_SUCCESS){
            return RespBean.success("激活成功");
        }else {
            return RespBean.success("重复激活");
        }
    }
    @ResponseBody
    @ApiOperation(value="用户登陆",notes="需要账户名和密码 网址格式为 /user/login/{username}/{password}")
    @PostMapping("/user/login/{username}/{password}")
    public RespBean login(HttpServletRequest req,@PathVariable("username") String userName,@PathVariable("password") String password){
        if (userName == null){
            return RespBean.error("请输入用户名");
        }
        if (password==null){
            return RespBean.error("请输入密码");
        }
        HttpSession session = req.getSession();
        User user = userService.login(userName, password);

        if (user == null){
            return RespBean.error("账号或者密码错误");
        }
        if (user.getActive().equals(Constants.USER_NOT_ACTIVE)){
            return RespBean.error("账号未激活");
        }
        //TODO 修改为swt
        session.setAttribute("loginUser",user);

        return RespBean.success("登陆成功",user.getNickName());
    }
}
