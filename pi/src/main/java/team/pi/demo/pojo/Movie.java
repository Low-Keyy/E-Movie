package team.pi.demo.pojo;

import lombok.Data;

import java.sql.Date;
import java.util.List;


@Data
public class Movie {
    private  int               id;

    public void setId(int id) {
        this.id = id;
    }

    private  int               tmdbid;
    private  String            original_language;
    private  String            original_title;
    private  String            overview;
    private  int               budget;
    private  int               adult;
    private  double            popularity;
    private String               release_date;
    private Long               revenue;
    private int                runtime;
    private String             status;
    private String             tagline;

    private String             title;
    private double             vote_average;
    private Long               vote_count;

    private String             poster_path;
    private int                deleted;


    /**属于的专辑名*/
    private MovieCollection    movieCollection;
    /**关键字*/
    private List<Keyword>      keywords;
    /**生产公司*/
    private List<Company>      companies;
    /**电影种类*/
    private List<Genres>       genres;
    /**生产国家*/
    private List<Country>      countries;
    /**口语*/
    private List<Language>     languages;
    /**演员*/
    private List<MovieCastRelation>         casts;
    /**工作人员*/
    private List<MovieCrewRelation>         crews;
}
