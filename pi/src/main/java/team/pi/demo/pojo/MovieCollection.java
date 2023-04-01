package team.pi.demo.pojo;


import lombok.Data;

import java.util.List;

@Data
public class MovieCollection {
    private int id;
    private int deleted;
    private String name;

    private List<Movie> movies;
}
