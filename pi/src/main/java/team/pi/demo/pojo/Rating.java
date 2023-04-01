package team.pi.demo.pojo;


import lombok.Data;

@Data
public class Rating {
    private  int    user_id;
    private  int    movie_id;
    private  float  rating;
    private  Long   timestamp;
    private  int    deleted;
}
