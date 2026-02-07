package aqa.torria.rangiffler.repository;

import aqa.torria.rangiffler.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    Optional<UserEntity> findByUsername(String username);

    @Query("select u from UserEntity u where u.username <> :currentUsername")
    Page<UserEntity> findAllExceptCurrent(@Param("currentUsername") String currentUsername,
                                          Pageable pageable);

    @Query("select u from UserEntity u where u.username <> :currentUsername" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
    Page<UserEntity> findAllExceptCurrent(@Param("currentUsername") String currentUsername,
                                          Pageable pageable,
                                          @Param("searchQuery") String searchQuery);

}
