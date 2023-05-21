package team.pi.demo.pojo;

import lombok.Data;

@Data
public class MovieCastRelation {

    private int         cast_id;
    private int         movie_id;
    private String      movie_title;
    private String      character;
    private int         order;
    private int         deleted;

    private Movie       movie;
    private Person      person;
}
