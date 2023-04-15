package team.pi.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import team.pi.demo.pojo.UserMovieCollect;
import team.pi.demo.pojo.UserMoviePlay;

@Mapper
public interface UserMovieCollectMapper {

    int insert(UserMovieCollect userMovieCollect);
}
