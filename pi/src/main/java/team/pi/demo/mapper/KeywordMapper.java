package team.pi.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.pi.demo.pojo.Movie;

import java.util.List;

@Mapper
public interface KeywordMapper {

    List<Movie> selectByKeywordName(@Param("keywordName") String name);
}
