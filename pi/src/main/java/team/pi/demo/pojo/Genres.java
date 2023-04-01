package team.pi.demo.pojo;


import lombok.Data;

import java.util.List;

@Data
public class Genres {
    private int     id;
    private String  name;
    private int     deleted;


    private List<Movie> movieList;
}
