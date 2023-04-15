package team.pi.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import team.pi.demo.pojo.UserMoviePlay;

@Mapper
public interface UserMoviePlayMapper {

    int insert(UserMoviePlay userMoviePlay);
}
