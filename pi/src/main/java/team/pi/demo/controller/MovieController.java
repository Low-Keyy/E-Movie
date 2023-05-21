package team.pi.demo.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import team.pi.demo.pojo.*;
import team.pi.demo.service.MovieService;
import team.pi.demo.utils.RandomUtils;

import java.util.Objects;

@RestController
@Api(value="电影controller",tags={"电影操作接口"})
public class MovieController {
    @Autowired
    MovieService movieService;
    @ApiOperation(value="根据电影名模糊查找电影")
    @GetMapping("/movies/{name}")
    public RespBean searchByVagueMovieName(@PathVariable("name")String vagueName){
        //TODO 异常处理
        if(Objects.isNull(vagueName))
            return RespBean.error("查询字符串为空");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getPrincipal().equals("anonymousUser"))
        {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            if(loginUser.getUser().getRole().equals("admin"))
                return movieService.AdminSearchByVagueMovieName(vagueName);
        }
        return movieService.searchByVagueMovieName(vagueName);
    }

    @ApiOperation(value="根据演员名查找电影")
    @GetMapping("/person/{name}/movies")
    public RespBean searchByPersonName(@PathVariable("name")String personName){
        //TODO 异常处理
        if(Objects.isNull(personName) || personName == "")
            return RespBean.error("查询字符串为空");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getPrincipal().equals("anonymousUser"))
        {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            if(loginUser.getUser().getRole().equals("admin"))
                return movieService.AdminsearchByPersonMovieName(personName);
        }
        return movieService.searchByPersonMovieName(personName);
    }

    @ApiOperation(value="根据公司名查找电影")
    @GetMapping("/companies/{name}/movies")
    public RespBean searchByCompanyName(@PathVariable("name")String companyName){
        //TODO 异常处理
        if(Objects.isNull(companyName))
            return RespBean.error("查询字符串为空");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getPrincipal().equals("anonymousUser"))
        {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            if(loginUser.getUser().getRole().equals("admin"))
                return movieService.AdminsearchByCompanyName(companyName);
        }
        return movieService.searchByCompanyName(companyName);
    }

    @ApiOperation(value="根据关键词查找电影")
    @GetMapping("/keywords/{name}/movies")
    public RespBean searchByKeyword(@PathVariable("name")String keyword){
        //TODO 异常处理
        if(Objects.isNull(keyword))
            return RespBean.error("查询字符串为空");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!authentication.getPrincipal().equals("anonymousUser"))
        {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            if(loginUser.getUser().getRole().equals("admin"))
                return movieService.AdminsearchByKeywordName(keyword);
        }
        return movieService.searchByKeywordName(keyword);
    }
    @ApiOperation(value="打分")
    @PostMapping("/movies/score")
    @PreAuthorize("hasAuthority('user')")
    public RespBean score(@RequestBody Rating rating){
        //TODO 异常处理
        if(Objects.isNull(rating))
            return RespBean.error("未传递数据");
        if(Objects.isNull(rating.getMovie_id()))
            return RespBean.error("电影id未传递");
        if(Objects.isNull(rating.getRating()) || rating.getRating()>5 || rating.getRating()<0)
            return RespBean.error("分数不达标");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        rating.setUsername(loginUser.getUsername());
        rating.setTimestamp(RandomUtils.getTime());
        return movieService.score(rating);
    }


    @ApiOperation(value="查分")
    @GetMapping("/movies/score")
    @PreAuthorize("hasAuthority('user')")
    public RespBean getScore(@RequestBody Rating rating){
        //TODO 异常处理
        if(Objects.isNull(rating))
            return RespBean.error("未传递数据");
        if(Objects.isNull(rating.getMovie_id()))
            return RespBean.error("电影id未传递");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        rating.setUsername(loginUser.getUsername());
        return movieService.getScore(rating);
    }
    @ApiOperation(value="获取元数据")
    @GetMapping("/movies/details/{id}")
    public RespBean queryMovieDetail(@PathVariable("id") int id){
        //TODO 异常处理
        if(Objects.isNull(id))
            return RespBean.error("获取失败请重新再试");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getPrincipal().equals("anonymousUser"))
        return movieService.selectMovieById(id);
        else{
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            return movieService.selectTokenMovieById(id,loginUser);
        }
    }

    @ApiOperation(value="推荐电影")
    @GetMapping("/movies/recommend")
    public RespBean movieRecommend(@RequestBody Page page){
        //TODO 异常处理
        if(Objects.isNull(page))
            return RespBean.error("获取失败请重新再试");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getPrincipal().equals("anonymousUser"))
            return movieService.recommend(page);
        else{
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            if(loginUser.getUser().getRole().equals("admin")) return movieService.recommend(page);
            return movieService.recommendToken(page,loginUser);
        }
    }

    @ApiOperation(value="电影修改")
    @PutMapping("/movies/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public RespBean movieUpdate(@PathVariable("id") Integer id,@RequestBody Movie movie){
        //TODO 异常处理
        return movieService.update(id,movie);
    }

    @ApiOperation(value="电影删除")
    @DeleteMapping("/movies/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public RespBean movieUpdate(@PathVariable("id") Integer id){
        //TODO 异常处理
        Movie movie = new Movie();
        movie.setDeleted(id);
        return movieService.update(id,movie);
    }
    @ApiOperation(value="插入新电影")
    @PostMapping("/movies")
    @PreAuthorize("hasAuthority('admin')")
    public RespBean movieInsert(@RequestBody Movie movie){
        //TODO 异常处理
        if (Objects.isNull(movie))
            return RespBean.error("输入为空");
        return movieService.insert(movie);
    }

    @ApiOperation(value="新增电影种类")
    @PostMapping("/genres")
    @PreAuthorize("hasAuthority('admin')")
    public RespBean genresInsert(@RequestBody Genres genres){
        //TODO 异常处理
        if (Objects.isNull(genres))
            return RespBean.error("输入为空");
        return movieService.genresInsert(genres);
    }

    @ApiOperation(value="搜索电影种类")
    @GetMapping("/genres")
    @PreAuthorize("hasAuthority('admin')")
    public RespBean genresSearch(@RequestBody Genres genres){
        //TODO 异常处理
        if (Objects.isNull(genres))
            return RespBean.error("输入为空");
        return movieService.genresSearch(genres);
    }

    @ApiOperation(value="在电影种类中加入电影")
    @PostMapping("/genres/movies")
    @PreAuthorize("hasAuthority('admin')")
    public RespBean genresAddMovie(@RequestBody GenresMovieRelation genresMovieRelation){
        //TODO 异常处理
        if (Objects.isNull(genresMovieRelation))
            return RespBean.error("输入为空");
        return movieService.genresAddMovie(genresMovieRelation);
    }

}
