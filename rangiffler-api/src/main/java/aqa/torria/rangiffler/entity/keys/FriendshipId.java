package aqa.torria.rangiffler.entity.keys;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EqualsAndHashCode
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendshipId implements Serializable {

    @Column(name = "requester_id", nullable = false)
    UUID requesterId;

    @Column(name = "addressee_id", nullable = false)
    UUID addresseeId;
}
