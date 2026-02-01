package aqa.torria.rangiffler.repository;

import aqa.torria.rangiffler.entity.PhotoEntity;
import aqa.torria.rangiffler.view.CountryStatView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface PhotoRepository extends JpaRepository<PhotoEntity, UUID> {

    @EntityGraph(attributePaths = "country")
    Page<PhotoEntity> findByUserId(UUID userId, Pageable pageable);

    @EntityGraph(attributePaths = "country")
    Page<PhotoEntity> findByUserIdIn(Collection<UUID> userIds, Pageable pageable);

    @Query("""
            select p.country as country, count(p.id) as count
            from PhotoEntity p
            where p.userId in :userIds
            group by p.country
            """)
    List<CountryStatView> countByCountry(@Param("userIds") Collection<UUID> userIds);
}
