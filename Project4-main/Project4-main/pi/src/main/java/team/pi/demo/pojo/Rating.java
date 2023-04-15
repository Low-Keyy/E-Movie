package team.pi.demo.pojo;


import lombok.Data;

import java.io.Serializable;

@Data
public class Rating {
    private  int    user_id;
    private  String username;
    private  int    movie_id;
    private  float  rating;
    private  Long   timestamp;
    private  int    deleted;
}
