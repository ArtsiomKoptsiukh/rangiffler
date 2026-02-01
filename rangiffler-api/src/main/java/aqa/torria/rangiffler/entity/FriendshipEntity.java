package aqa.torria.rangiffler.entity;

import aqa.torria.rangiffler.entity.keys.FriendshipId;
import aqa.torria.rangiffler.model.FriendshipStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "friendship")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FriendshipEntity {

    @EmbeddedId
    FriendshipId id;

    @MapsId("requesterId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requester_id", nullable = false)
    UserEntity requester;

    @MapsId("addresseeId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "addressee_id", nullable = false)
    UserEntity addressee;

    @Column(name = "created_date", nullable = false)
    LocalDateTime createdDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    FriendshipStatus status;

    public FriendshipEntity(UserEntity requester,
                            UserEntity addressee,
                            LocalDateTime createdDate,
                            FriendshipStatus status) {
        this.requester = requester;
        this.addressee = addressee;
        this.createdDate = createdDate;
        this.status = status;
        this.id = new FriendshipId(requester.getId(), addressee.getId());
    }
}
