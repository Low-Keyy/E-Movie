package team.pi.demo.service;


import team.pi.demo.pojo.Movie;
import team.pi.demo.pojo.Rating;
import team.pi.demo.pojo.RespBean;

public interface MovieService {
    RespBean searchByVagueMovieName(String vagueName);


    RespBean searchByPersonMovieName(String vagueName);

    RespBean searchByCompanyName(String companyName);

    RespBean searchByKeywordName(String keyword);

    RespBean score(Rating rating);

    RespBean selectMovieById(int id);
}
