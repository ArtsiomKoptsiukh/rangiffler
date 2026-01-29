package aqa.torria.rangiffler.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class Feed {
    private String username;
    private Boolean withFriends;
    private Page<Photo> photos;
    private List<Stat> stat;
}
