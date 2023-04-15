package team.pi.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMoviePlay {
    private String userName;
    private int user_id;
    private int movie_id;
    private Long startTime;
    private Long endTime;
}
