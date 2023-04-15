package team.pi.demo.pojo;

import lombok.Data;

@Data
public class AlRating {
    private  int    user_id;
    private  String username;
    private  int    movie_id;
    private  float  rating;
    private  Long   timestamp;
    private  int    deleted;

    public static AlRating parseRating(String str){
//            str = str.replace("\"","");
        String[] strArr = str.split(",");
        int userId = Integer.parseInt(strArr[0]);
        int itemId = Integer.parseInt(strArr[1]);
        int rating = Integer.parseInt(strArr[2]);

        return new AlRating(userId,itemId,rating);
    }

    public AlRating()
    {

    }
    public AlRating(int userId, int itemId, int rating) {
        this.user_id = userId;
        this.movie_id = itemId;
        this.rating = rating;
    }

}
