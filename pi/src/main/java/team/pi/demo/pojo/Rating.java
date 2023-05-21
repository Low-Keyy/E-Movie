package team.pi.demo.pojo;


import lombok.Data;

@Data
public class Rating {
    private  int    user_id;
    private  String username;
    private  int    movie_id;
    private  float  rating;
    private  String   timestamp;
    private  int    deleted;
}
