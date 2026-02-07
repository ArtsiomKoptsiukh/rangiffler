package aqa.torria.rangiffler.model;

import lombok.Data;

@Data
public class PhotoInput {
    private String id;
    private String src;
    private CountryInput country;
    private String description;
    private LikeInput like;
}
