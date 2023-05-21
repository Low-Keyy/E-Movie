package team.pi.demo.service;


import team.pi.demo.pojo.*;

public interface MovieService {
    RespBean searchByVagueMovieName(String vagueName);


    RespBean searchByPersonMovieName(String vagueName);

    RespBean searchByCompanyName(String companyName);

    RespBean searchByKeywordName(String keyword);

    RespBean score(Rating rating);

    RespBean selectMovieById(int id);

    RespBean getScore(Rating rating);

    RespBean selectTokenMovieById(int id, LoginUser loginUser);

    RespBean selectUserHistories(Page page,LoginUser loginUser);

    RespBean deleteUserHistories(UserMoviePlay userMoviePlay, LoginUser loginUser);

    RespBean recommend(Page page);

    RespBean recommendToken(Page page, LoginUser loginUser);

    RespBean update(Integer id, Movie movie);

    RespBean insert(Movie movie);

    RespBean genresInsert(Genres genres);

    RespBean genresSearch(Genres genres);

    RespBean genresAddMovie(GenresMovieRelation genresMovieRelation);

    RespBean AdminSearchByVagueMovieName(String vagueName);

    RespBean AdminsearchByPersonMovieName(String personName);

    RespBean AdminsearchByCompanyName(String companyName);

    RespBean AdminsearchByKeywordName(String keyword);
}
