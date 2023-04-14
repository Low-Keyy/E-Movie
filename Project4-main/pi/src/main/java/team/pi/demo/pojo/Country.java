package team.pi.demo.pojo;

import lombok.Data;

@Data
public class Country {
    private int         id;
    private String      iso;
    private String      name;
    private int         deleted;
}
