package team.pi.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import team.pi.demo.pojo.Movie;
import team.pi.demo.pojo.Rating;
import team.pi.demo.pojo.RespBean;
import team.pi.demo.service.MovieService;

@RestController
public class MovieController {
    @Autowired
    MovieService movieService;

    @GetMapping("/movies")
    public RespBean searchByVagueMovieName(@RequestParam("name")String vagueName){
        //TODO 异常处理
        RespBean respBean=  movieService.searchByVagueMovieName(vagueName);
        return respBean;
    }

    @GetMapping("/person/{name}/movies")
    public RespBean searchByPersonName(@PathVariable("name")String personName){
        //TODO 异常处理
        RespBean respBean=  movieService.searchByPersonMovieName(personName);
        return respBean;
    }

    @GetMapping("/companies/{name}/movies")
    public RespBean searchByCompanyName(@PathVariable("name")String companyName){
        //TODO 异常处理
        RespBean respBean=  movieService.searchByCompanyName(companyName);
        return respBean;
    }

    @GetMapping("/keywords/{name}/movies")
    public RespBean searchByKeyword(@PathVariable("name")String keyword){
        //TODO 异常处理
        RespBean respBean=  movieService.searchByKeywordName(keyword);
        return respBean;
    }

    @PostMapping("/movies/score")
    public RespBean score(@RequestBody Rating rating){
        //TODO 异常处理
        RespBean respBean=  movieService.score(rating);
        return respBean;
    }

    @GetMapping("/movies/details/{id}")
    public RespBean queryMovieDetail(@PathVariable("id") int id){
        //TODO 异常处理
        RespBean respBean=  movieService.selectMovieById(id);
        return respBean;
    }
}
