package aqa.torria.rangiffler.repository;

import aqa.torria.rangiffler.entity.PhotoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhotoRepository extends JpaRepository<PhotoEntity, UUID> {

    @EntityGraph(attributePaths = "country")
    Page<PhotoEntity> findByUserId(UUID userId, Pageable pageable);
}
