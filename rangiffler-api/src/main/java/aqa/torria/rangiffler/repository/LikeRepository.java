package aqa.torria.rangiffler.repository;

import aqa.torria.rangiffler.entity.LikeEntity;
import aqa.torria.rangiffler.view.PhotoLikeView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<LikeEntity, UUID> {

    @Query(value = """
    select l.user_id as userId,
           u.username as username,
           l.created_date as createdDate
    from photo_like pl
    join `like` l on l.id = pl.like_id
    join `user` u on u.id = l.user_id
    where pl.photo_id = :photoId
    order by l.created_date desc
    """, nativeQuery = true)
    List<PhotoLikeView> findLikesByPhotoId(@Param("photoId") UUID photoId);

    @Query(value = """
    select count(*)
    from photo_like pl
    where pl.photo_id = :photoId
    """, nativeQuery = true)
    long countLikesByPhotoId(@Param("photoId") UUID photoId);

}
