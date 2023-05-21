package team.pi.demo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.pi.demo.pojo.Genres;
import team.pi.demo.pojo.GenresMovieRelation;

import java.util.List;

@Mapper
public interface GenreMapper {

    int insertAll(@Param("name") String name);

    List<Genres> selectByname(@Param("name") String name);

    GenresMovieRelation selectByGenresMovieRelation(GenresMovieRelation genresMovieRelation);

    int insertRelation(GenresMovieRelation genresMovieRelation);
}
