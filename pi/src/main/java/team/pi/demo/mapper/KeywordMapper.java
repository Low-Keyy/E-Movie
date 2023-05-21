package team.pi.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.pi.demo.pojo.Keyword;
import team.pi.demo.pojo.Movie;

import java.util.List;

@Mapper
public interface KeywordMapper {

    List<Keyword> selectByKeywordName(@Param("keywordName") String name);
}
