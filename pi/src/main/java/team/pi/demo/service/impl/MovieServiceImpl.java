package team.pi.demo.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.pi.demo.mapper.*;
import team.pi.demo.pojo.*;
import team.pi.demo.service.MovieService;

import java.util.List;

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
    KeywordMapper keywordMapper;

    @Autowired
    RatingMapper ratingMapper;
    @Override
    public RespBean searchByVagueMovieName(String vagueName) {
        List<Movie> movies = movieMapper.selectByVagueMovieName(vagueName);

        return new RespBean(RESP_SUCCESS,"搜索成功",movies);
    }

    @Override
    public RespBean searchByPersonMovieName(String personName) {
        List<Person> people = personMapper.selectByName(personName);
        return new RespBean(RESP_SUCCESS,"搜索成功",people);
    }

    @Override
    public RespBean searchByCompanyName(String companyName) {
        Company companies = companyMapper.selectByCompanyName(companyName);
        return new RespBean(RESP_SUCCESS,"搜索成功",companies);
    }

    @Override
    public RespBean searchByKeywordName(String keyword) {
        List<Movie> movies = keywordMapper.selectByKeywordName(keyword);
        return new RespBean(RESP_SUCCESS,"搜索成功",movies);
    }

    @Override
    public RespBean score(Rating rating) {
        //TODO 打分
        User user=userMapper.selectUserByname(rating.getUsername());
        rating.setUser_id(user.getId());
        int insertCount = ratingMapper.insert(rating);
        if(insertCount>0){
            return  new RespBean(RESP_SUCCESS,"打分成功",null);
        }
        return  new RespBean(NOT_IMPLEMENTED,"打分失败",null);
    }

    @Override
    public RespBean selectMovieById(int id) {
        Movie movie = movieMapper.selectByMovieId(String.valueOf(id));
        return new RespBean(RESP_SUCCESS,"查询成功",movie);
    }


}
