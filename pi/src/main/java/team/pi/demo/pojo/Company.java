package team.pi.demo.pojo;


import lombok.Data;

import java.util.List;

@Data
public class Company {

    private int     id;
    private String  name;
    private int     deleted;

    public List<Keyword> movieList;

}
