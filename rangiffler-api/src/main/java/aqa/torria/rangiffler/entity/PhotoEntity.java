package aqa.torria.rangiffler.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "photo")
public class PhotoEntity {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity country;

    @Column(name = "description")
    private String description;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "photo", nullable = false)
    private byte[] photo;

    @Column(name = "created_date", nullable = false)
    private OffsetDateTime createdDate;
}
