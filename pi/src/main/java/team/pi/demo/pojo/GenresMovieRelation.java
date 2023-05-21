package team.pi.demo.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GenresMovieRelation {
    private int movie_id;
    private int genre_id;
}
