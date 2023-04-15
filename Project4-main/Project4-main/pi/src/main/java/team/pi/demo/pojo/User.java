package team.pi.demo.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "user实体类")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "用户id")
    private Integer id;
    @ApiModelProperty(value = "用户昵称")
    private String nickName;
    @ApiModelProperty(value = "用户名")
    private String userName;
    @ApiModelProperty(value = "用户密码")
    private String password;
    @ApiModelProperty(value = "用户头像图片url")
    private String avartar;
    @ApiModelProperty(value = "删除状态")
    private Integer deleted;
    @ApiModelProperty(value = "用户邮箱")
    private String email;
    @ApiModelProperty(value = "专辑名")
    private Integer collection;
    @ApiModelProperty(value = "用户状态，0表示未激活，1表示激活")
    private Integer active;
    @ApiModelProperty(value = "用户邮箱激活码")
    private String code;

    private List<Movie> movieCollect;
    private List<Movie> moviePlay;
}
