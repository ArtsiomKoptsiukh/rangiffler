package aqa.torria.rangiffler.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Photo {
    private String id;
    private String src;
    private Country country;
    private String description;
    private Date creationDate;
    private Likes likes;
    private Boolean isOwner;
}
