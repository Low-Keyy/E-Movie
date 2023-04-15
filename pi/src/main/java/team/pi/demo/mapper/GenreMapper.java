package team.pi.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GenreMapper {

    int insertAll(@Param("name") String name);
}
