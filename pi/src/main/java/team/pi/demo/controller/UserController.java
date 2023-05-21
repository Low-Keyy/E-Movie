package team.pi.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.pi.demo.pojo.*;
import team.pi.demo.service.MovieService;
import team.pi.demo.service.UserService;
import team.pi.demo.utils.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController

@Api(value="用户controller",tags={"用户操作接口"})
public class UserController {


    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder bCryptPasswordEncoder;
    @Autowired
    MovieService movieService;
    @ResponseBody
    @ApiOperation(value="用户注册",notes="输入用户名，昵称,密码，确认密码，邮箱地址,网址格式为 /user/register\n" +
            "需要json传入{userName} {password1} {password2} {email} {nickName}信息")
    @PostMapping("/users/register")
    //TODO 匿名访问
    public RespBean register(@RequestBody RegisterUser registerUser){
        String name =registerUser.getUserName();
        String password1=registerUser.getPassword1();
        String password2=registerUser.getPassword2();
        String mail=registerUser.getEmail();
        String nickname =registerUser.getNickName();
        if(name==null || password1==null || password2==null || mail==null || nickname==null){
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


        registerUser.setActive(Constants.USER_NOT_ACTIVE);//默认未被激活
        registerUser.setCode(RandomUtils.createActive());//邮箱激活代码

        registerUser.setPassword(bCryptPasswordEncoder.encode(registerUser.getPassword1()));//加密密码
        //TODO
        //用户头像设为默认地址
        registerUser.setAvartar(registerUser.getUserName()+".png");
        int i =userService.registerUser(registerUser);
        if (i==0){
            return RespBean.success("注册失败，请检查网络连接");
        }
        return RespBean.success("注册成功，请激活账户");
    }
    @ResponseBody
    @ApiOperation(value="激活账户",notes="无需输入，均由系统自动生成，网址格式为 /user/active?username={}&c={}")
    @GetMapping("/users/active")
    public RespBean active(@ApiParam("用户名") @RequestParam("username") String username,@ApiParam("激活码") @RequestParam("c") String c){
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
    @ApiOperation(value="用户登陆",notes="需要账户名和密码 网址格式为 /users/login\n" +
            "json传输\n{\n" +
            "   userName\n" +
            "   password\n}" +
            "传出两个token\n分别为\n" +
            "{\n" +
            "   accessToken\n" +
            "refreshToken\n" +
            "}")
    @PostMapping("/users/login")
    public RespBean login(HttpServletRequest req,@RequestBody User user){

        String userName=user.getUserName();
        String password=user.getPassword();

        if (userName == null){
            return RespBean.error("请输入用户名");
        }
        if (password==null){
            return RespBean.error("请输入密码");
        }

//        HttpSession session = req.getSession();
//        User user1 = userService.login(userName, password);
//
//        if (user1 == null){
//            return RespBean.error("账号或者密码错误");
//        }
//        if (user1.getActive().equals(Constants.USER_NOT_ACTIVE)){
//            return RespBean.error("账号未激活");
//        }
//
//        session.setAttribute("loginUser",user1);
//        return RespBean.success("登陆成功",user1.getNickName());
        return userService.login(user);
    }


    @ApiOperation(value="刷新token时效",notes="网址格式为 /users/refresh\n" +
            "在header中写入token，后端检查token时效，并传出两个新token")
    @ApiImplicitParam(name="token",value="accessToken 或者 refreshToken",required = true,paramType = "header")
    @GetMapping("/users/refresh")
    public  RespBean refreshToken(HttpServletRequest request){
        String token = request.getHeader("token");
        return userService.refreshToken(token);
    }


    @ApiOperation(value="更新用户信息",notes="网址格式为 /users\n" +
            "以token形式传输\n在json上对对应键值对修改即可")
    @PutMapping ("/users")
    @PreAuthorize("hasAuthority('user')")
    public  RespBean updateUserInfo(@RequestBody User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if (!loginUser.getUser().getUserName().equals(user.getUserName()))
        {
            return RespBean.error("token与用户名不对应");
        }
        return userService.updateUserInfo(user);
    }

    @ApiOperation(value="插入一条播放历史记录")
    @PostMapping ("/users/playMovies")
    public  RespBean insertPlayRecord(@RequestBody UserMoviePlay userMoviePlay){
        return userService.insertPlayRecord(userMoviePlay);
    }
    @ApiOperation(value="插入一条收藏记录",notes = "用户可对影片进行收藏")
    @PostMapping ("/users/collectedMovies")
    public  RespBean insertPlayRecord(@RequestBody UserMovieCollect userMovieCollect){
        return userService.insertCollectRecord(userMovieCollect);
    }
    @ResponseBody
    @PostMapping("/users/hello")
//    @PreAuthorize("hasAuthority('user')")
    public  RespBean hello(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        if(authentication.getPrincipal().equals("anonymousUser"))
            System.out.println("123121");
//        LoginUser loginUser = (LoginUser)
//        authentication.getPrincipal();
        return new RespBean(200,"hello",null);
    }

    @ResponseBody
    @PostMapping("/users/verify")
    @ApiOperation(value="修改密码的验证操作",notes = "验证")
    public RespBean verify(@RequestBody(required = false) User user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        // 匿名访问 即忘记密码
        if(authentication.getPrincipal().equals("anonymousUser"))
        {
            //判空
            if(Objects.isNull(user.getUserName()))
                return RespBean.error("未输入用户名");
            return userService.verify(user.getUserName());
        }else {
            // 携带jwt的访问
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            return userService.verify(loginUser.getUser().getUserName());
        }
    }

    @ResponseBody
    @PutMapping("/users/changepsw")
    @ApiOperation(value="修改密码",notes = "修改密码")
    public RespBean changepsw(@RequestBody(required = false) VerifyUser user){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 匿名访问
        if(authentication.getPrincipal().equals("anonymousUser"))
        {
            //判空
            if(Objects.isNull(user.getUserName()))
                return RespBean.error("未输入用户名");
            if(Objects.isNull(user.getVerifyCode()))
                return RespBean.error("未输入验证码");
            if(Objects.isNull(user.getPassword()))
                return RespBean.error("未输入密码");
            if (user.getPassword().length()<6 || user.getPassword().length()>24){
                return RespBean.error("密码长度位6-24个字符");
            }
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return userService.changepsw(user);
        }else {
            // 携带jwt的访问
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            if(Objects.isNull(user.getVerifyCode()))
                return RespBean.error("未输入验证码");
            if(Objects.isNull(user.getPassword()))
                return RespBean.error("未输入密码");
            if (user.getPassword().length()<6 || user.getPassword().length()>24){
                return RespBean.error("密码长度位6-24个字符");
            }
            user.setUserName(loginUser.getUser().getUserName());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return userService.changepsw(user);
        }
    }


    @ApiOperation(value="更改用户头像")
    @PutMapping("/users/changeAvartars")
    @PreAuthorize("hasAuthority('user')")
    public RespBean changeAvartars(@RequestParam("file") MultipartFile file){
        if (file.isEmpty())
            return RespBean.error("文件为空");
        if(!file.getOriginalFilename().endsWith(".png"))
            return RespBean.error("文件格式错误");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return userService.changeAvartars(file, loginUser);
    }

    @ApiOperation(value="查询用户信息")
    @GetMapping("/users")
    @PreAuthorize("hasAuthority('user')")
    public RespBean userInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return userService.userInfo(loginUser);
    }

    @ApiOperation(value = "登出")
    @DeleteMapping("/users/logout")
    @PreAuthorize("hasAnyAuthority('user','admin')")
    public RespBean logout(HttpServletRequest request)
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        String token = request.getHeader("token");
        return userService.logout(loginUser,token);
    }

    @ApiOperation(value="分页查询播放历史")
    @GetMapping("/users/movies")
    @PreAuthorize("hasAuthority('user')")
    public RespBean queryMovieUser(@RequestBody Page page){
        //TODO 异常处理
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return movieService.selectUserHistories(page,loginUser);
    }

    @ApiOperation(value="删除指定的播放历史")
    @DeleteMapping("/users/movies")
    @PreAuthorize("hasAuthority('user')")
    public RespBean queryMovieUser(@RequestBody UserMoviePlay userMoviePlay){
        //TODO 异常处理
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        return movieService.deleteUserHistories(userMoviePlay,loginUser);
    }
}
