package team.pi.demo.utils;

//项目的常量类
public class Constants {
    /**
     * 定义用户模块涉及的常量
     */
    public static final String projectPath = System.getProperty("user.dir");

    public static final String HAS_USER = "1";

    public static final String NOT_HAS_USER = "0";

    public static final Integer USER_ACTIVE = 1;

    public static final Integer USER_NOT_ACTIVE = 0;

    public static final int NAME_OR_MAIL_ERROR = 0;

    public static final int NAME_AND_MAIL_CORRECT = 1;

    public static final int UNKNOW_ERROR = 500;

    public static final int VERIFY_TRUE = 1;

    public static final int VERIFY_FALSE = 0;

    public static final int UPDATE_PASSWORD_SUCCESS = 1;

    public static final int UPDATE_PASSWORD_FAIL = 0;

    /**
     * 用户模块激活状态
     */
    public static final int ACTIVE_FAIL = 0;
    public static final int ACTIVE_SUCCESS = 1;
    public static final int ACTIVE_ALREADY = 2;

    public static final int TOKEN_EXPIRED = 401;

    public static final int UPDATE_TOKEN_SUCCESS = 1;
    public static final int TOKEN_VALIDITY = 300000000;    //单位秒 60*5


    public static final int RESP_SUCCESS =200;

    public static final int  NOT_IMPLEMENTED =501;

}
