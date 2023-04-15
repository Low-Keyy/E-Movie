package team.pi.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.pi.demo.pojo.User;

import java.util.List;

@Mapper
public interface UserMapper {

    List<User> findAll();


    User selectUserByname(@Param("username") String username);

    int insertUser(@Param("user") User user);

    User selectUserByCode(@Param("code") String code);

    int updateStatusByUserName(@Param("username") String userName);

    int updateUser(User user);
}
