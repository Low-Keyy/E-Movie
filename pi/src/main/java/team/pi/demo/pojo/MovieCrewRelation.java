package team.pi.demo.pojo;

import lombok.Data;

import java.util.List;

@Data
public class MovieCrewRelation {

    private int         crew_id;
    private int         movie_id;
    private String      department;
    private String      job;
    private int         deleted;

    private List<Movie> movieList;
    private Person      person;
}
