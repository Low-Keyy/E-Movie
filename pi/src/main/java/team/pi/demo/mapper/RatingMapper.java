package team.pi.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import team.pi.demo.pojo.Rating;

@Mapper
public interface RatingMapper {

    int insert(Rating rating);

    int update(Rating rating);

    Double select(Rating rating);

}
