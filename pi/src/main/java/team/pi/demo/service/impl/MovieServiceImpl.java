package team.pi.demo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.pi.demo.mapper.*;
import team.pi.demo.pojo.*;
import team.pi.demo.service.MovieService;
import team.pi.demo.utils.RandomUtils;

import java.util.List;
import java.util.Objects;

import static team.pi.demo.utils.Constants.NOT_IMPLEMENTED;
import static team.pi.demo.utils.Constants.RESP_SUCCESS;

@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    MovieMapper movieMapper;

    @Autowired
    PersonMapper personMapper;

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    GenreMapper genreMapper;

    @Autowired
    KeywordMapper keywordMapper;

    @Autowired
    RatingMapper ratingMapper;
    @Autowired
    UserMoviePlayMapper userMoviePlayMapper;
    @Override
    public RespBean searchByVagueMovieName(String vagueName) {
        List<Movie> movies = movieMapper.selectByVagueMovieName(vagueName);
        if(!Objects.isNull(movies)){
        if(movies.size() > 10) movies=movies.subList(0,10);
        //删除失效影片
        movies.removeIf(movie -> movie.getDeleted()!=0);}
        return new RespBean(RESP_SUCCESS,"搜索成功",movies);
    }

    @Override
    public RespBean searchByPersonMovieName(String personName) {
        List<Person> people = personMapper.selectByName(personName);
        if(!Objects.isNull(people)){
        if(people.size() > 10) people=people.subList(0,10);
        for(Person p:people)
        {
            if(p.getMovieCastRelations().size() > 10) p.setMovieCastRelations(p.getMovieCastRelations().subList(0,10));
            if(p.getMovieCrewRelations().size()>10) p.setMovieCrewRelations(p.getMovieCrewRelations().subList(0,10));
        }}

        return new RespBean(RESP_SUCCESS,"搜索成功",people);
    }

    @Override
    public RespBean searchByCompanyName(String companyName) {
        Company companies = companyMapper.selectByCompanyName(companyName);
        if(!Objects.isNull(companies)) {
            if (companies.getMovieList().size() > 10) companies.setMovieList(companies.getMovieList().subList(0, 10));
        //删除失效
            companies.movieList.removeIf(movie -> movie.getDeleted() != 0);
        }
        return new RespBean(RESP_SUCCESS,"搜索成功",companies);
    }

    @Override
    public RespBean searchByKeywordName(String keyword) {
        List<Keyword> keywords = keywordMapper.selectByKeywordName(keyword);
        if(!Objects.isNull(keywords)){
            if(keywords.size() > 10) keywords=keywords.subList(0,10);
            for(Keyword k:keywords){
                if(k.getMovies().size()>10) k.setMovies(k.getMovies().subList(0,10));
                k.movies.removeIf(movie -> movie.getDeleted()!=0);
            }
        //删除失效影片
        }
        return new RespBean(RESP_SUCCESS,"搜索成功",keywords);
    }
    @Override
    public RespBean AdminSearchByVagueMovieName(String vagueName) {
        List<Movie> movies = movieMapper.selectByVagueMovieName(vagueName);
        if(!Objects.isNull(movies)){
            if(movies.size() > 10) movies=movies.subList(0,10);
            }
        return new RespBean(RESP_SUCCESS,"搜索成功",movies);
    }

    @Override
    public RespBean AdminsearchByPersonMovieName(String personName) {
        List<Person> people = personMapper.selectByName(personName);
        if(!Objects.isNull(people)){
            if(people.size() > 10) people=people.subList(0,10);
            for(Person p:people)
            {
                if(p.getMovieCastRelations().size() > 10) p.setMovieCastRelations(p.getMovieCastRelations().subList(0,10));
                if(p.getMovieCrewRelations().size()>10) p.setMovieCrewRelations(p.getMovieCrewRelations().subList(0,10));
            }}

        return new RespBean(RESP_SUCCESS,"搜索成功",people);
    }

    @Override
    public RespBean AdminsearchByCompanyName(String companyName) {
        Company companies = companyMapper.selectByCompanyName(companyName);
        if(!Objects.isNull(companies)) {
            if (companies.getMovieList().size() > 10) companies.setMovieList(companies.getMovieList().subList(0, 10));
        }
        return new RespBean(RESP_SUCCESS,"搜索成功",companies);
    }

    @Override
    public RespBean AdminsearchByKeywordName(String keyword) {
        List<Keyword> keywords = keywordMapper.selectByKeywordName(keyword);
        if(!Objects.isNull(keywords)){
            if(keywords.size() > 10) keywords=keywords.subList(0,10);
            for(Keyword k:keywords){
                if(k.getMovies().size()>10) k.setMovies(k.getMovies().subList(0,10));
            }
            //删除失效影片
        }
        return new RespBean(RESP_SUCCESS,"搜索成功",keywords);
    }
    @Override
    public RespBean score(Rating rating) {
        //TODO 打分
        User user=userMapper.selectUserByname(rating.getUsername());
        rating.setUser_id(user.getId());
        Double rate = ratingMapper.select(rating);
        if(rate==null){
            int insertCount = ratingMapper.insert(rating);
            if(insertCount > 0) return  new RespBean(RESP_SUCCESS,"打分成功",null);
        }else{
            int update = ratingMapper.update(rating);
            if(update > 0) return  new RespBean(RESP_SUCCESS,"打分成功",null);
        }
        return  new RespBean(NOT_IMPLEMENTED,"打分失败",null);
    }

    @Override
    public RespBean selectMovieById(int id) {
        List<Movie> movie = movieMapper.selectByMovieId(String.valueOf(id));
        if (movie.size()>0 && movie.get(0).getDeleted()==0)
        return new RespBean(RESP_SUCCESS,"查询成功",movie.get(0));
        else return RespBean.error("该电影不存在");
    }

    @Override
    public RespBean getScore(Rating rating) {
        User user=userMapper.selectUserByname(rating.getUsername());
        rating.setUser_id(user.getId());
        Double rate = ratingMapper.select(rating);
        if(rate==null) rate=0.0;
        return RespBean.success(Double.toString(rate));
    }

    @Override
    public RespBean selectTokenMovieById(int id, LoginUser loginUser) {
        List<Movie> movie = movieMapper.selectByMovieId(String.valueOf(id));
        if (movie.size()>0 && movie.get(0).getDeleted()==0)
        {
            User user = userMapper.selectUserByname(loginUser.getUsername());
            UserMoviePlay userMoviePlay = new UserMoviePlay();
            userMoviePlay.setUser_id(user.getId());
            userMoviePlay.setMovie_id(id);
            userMoviePlay.setTime(RandomUtils.getTime());
            userMoviePlayMapper.insert(userMoviePlay);
            return new RespBean(RESP_SUCCESS,"查询成功",movie.get(0));
        }
        else return RespBean.error("该电影不存在");
    }

    @Override
    public RespBean selectUserHistories(Page page,LoginUser loginUser) {
        User user=userMapper.selectUserByname(loginUser.getUsername());
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<UserMoviePlay> userAll = userMoviePlayMapper.findUserAll(user.getId());
        userAll.removeIf(userMoviePlay -> userMoviePlay.getDeleted()!=0);
        return RespBean.success("成功",new PageInfo<>(userAll));
    }

    @Override
    public RespBean deleteUserHistories(UserMoviePlay userMoviePlay, LoginUser loginUser) {
        User user=userMapper.selectUserByname(loginUser.getUsername());
        userMoviePlay.setUser_id(user.getId());
        System.out.println(userMoviePlay);
        UserMoviePlay userMoviePlay1 = userMoviePlayMapper.selectUserMovie(userMoviePlay);
        if(Objects.isNull(userMoviePlay1)) return RespBean.success("成功");
        userMoviePlayMapper.delete(userMoviePlay);
        return RespBean.success("成功");
    }

    @Override
    public RespBean recommend(Page page) {
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<Movie> all = movieMapper.findAll();
        all.removeIf(movie -> movie.getDeleted()!=0);
        return RespBean.success("成功",new PageInfo<>(all));
    }

    @Override
    public RespBean recommendToken(Page page, LoginUser loginUser) {
        //TODO
        PageHelper.startPage(page.getPageNum(), page.getPageSize());
        List<Movie> all = movieMapper.findAll();
        all.removeIf(movie -> movie.getDeleted()!=0);
        return RespBean.success("成功",new PageInfo<>(all));
    }

    @Override
    public RespBean update(Integer id, Movie movie) {
        movie.setId(id);
        int i = movieMapper.updateMovie(movie);
        if(i>0)
        return RespBean.success("修改成功");
        else return RespBean.error("修改失败,未知原因");
    }

    @Override
    public RespBean insert(Movie movie) {
        int i = movieMapper.insertAll(movie);
        if(i>0)
            return RespBean.success("插入成功");
        else return RespBean.error("插入失败,未知原因");
    }

    @Override
    public RespBean genresInsert(Genres genres) {
        int i = genreMapper.insertAll(genres.getName());
        if(i>0)
            return RespBean.success("插入成功");
        else return RespBean.error("插入失败,未知原因");
    }

    @Override
    public RespBean genresSearch(Genres genres) {
        List<Genres> genres1 = genreMapper.selectByname(genres.getName());
        return RespBean.success("查询成功",genres1);
    }

    @Override
    public RespBean genresAddMovie(GenresMovieRelation genresMovieRelation) {
        //是否重复
        GenresMovieRelation genresMovieRelation1 = genreMapper.selectByGenresMovieRelation(genresMovieRelation);
        //不重复 插入
        if(Objects.isNull(genresMovieRelation1)) {
            int i = genreMapper.insertRelation(genresMovieRelation);
            if(i>0)
                return RespBean.success("插入成功");
            else return RespBean.error("插入失败,未知原因");
        }
        return RespBean.success("成功");
    }



}
