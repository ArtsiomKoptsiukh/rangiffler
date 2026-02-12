package aqa.torria.rangiffler.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@Builder
public class User {
    private String id;
    private String username;
    private String firstname;
    private String surname;
    private String avatar;
    private FriendStatus friendStatus;
    private Country location;

    private Page<User> friends;
    private Page<User> incomeInvitations;
    private Page<User> outcomeInvitations;
}
