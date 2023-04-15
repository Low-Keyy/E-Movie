package team.pi.demo.pojo;

import lombok.Data;

import java.util.List;


@Data
public class Keyword {

    private Long  id;
    private String   name;
    private int   deleted;

    private List<Movie> movies;
}
