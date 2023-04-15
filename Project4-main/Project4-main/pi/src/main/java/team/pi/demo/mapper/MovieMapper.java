package team.pi.demo.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import team.pi.demo.pojo.Movie;

import java.util.List;

@Mapper
public interface MovieMapper {

    List<Movie> selectByMovieName(@Param("title") String title) ;

    Movie selectByMovieId(@Param("id") String id) ;

    List<Movie> selectByVagueMovieName(@Param("title") String title) ;

    int insertAll(Movie movie);


    int updateMovie(Movie movie);
}
