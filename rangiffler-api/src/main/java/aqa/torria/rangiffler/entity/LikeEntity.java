package aqa.torria.rangiffler.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "`like`")
public class LikeEntity {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    UUID id;

    @Column(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    UUID userId;

    @Column(name = "created_date", nullable = false)
    OffsetDateTime createdDate;
}
