package team.pi.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMovieCollect {
    private String userName;
    private int user_id;
    private int movie_id;
}
