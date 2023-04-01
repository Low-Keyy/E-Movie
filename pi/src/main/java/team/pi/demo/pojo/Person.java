package team.pi.demo.pojo;

import lombok.Data;

import java.util.List;

@Data
public class Person {
    private int         id;
    private String      name;
    private int         gender;
    private String      profile_path;
    private int         deleted;


    private List<MovieCastRelation> movieCastRelations;
    private List<MovieCrewRelation> movieCrewRelations;
}
