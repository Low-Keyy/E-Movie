package team.pi.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.pi.demo.pojo.UserMoviePlay;

import java.util.List;

@Mapper
public interface UserMoviePlayMapper {

    int insert(UserMoviePlay userMoviePlay);
    int delete(UserMoviePlay userMoviePlay);
    List<UserMoviePlay> findUserAll(@Param("userid")Integer userid);
    UserMoviePlay selectUserMovie(UserMoviePlay UserMoviePlay);
}
