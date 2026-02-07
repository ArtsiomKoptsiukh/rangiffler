package aqa.torria.rangiffler.repository;

import aqa.torria.rangiffler.entity.FriendshipEntity;
import aqa.torria.rangiffler.entity.UserEntity;
import aqa.torria.rangiffler.entity.keys.FriendshipId;
import aqa.torria.rangiffler.model.FriendshipStatus;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FriendshipRepository extends JpaRepository<FriendshipEntity, FriendshipId> {

    @Query("""
        select case
            when f.requester.id = :myId then f.addressee.id
            else f.requester.id
        end
        from FriendshipEntity f
        where f.status = :status
          and (f.requester.id = :myId or f.addressee.id = :myId)
        """)
    List<UUID> findFriendIds(@Param("myId") UUID myId,
                             @Param("status") FriendshipStatus status);

    Optional<FriendshipEntity> findByRequesterAndAddressee(UserEntity requester, UserEntity addressee);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
            " where f.status = aqa.torria.rangiffler.model.FriendshipStatus.ACCEPTED and f.requester = :requester")
    Page<UserEntity> findFriends(@Param("requester") UserEntity requester,
                                 @Nonnull Pageable pageable);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
            " where f.status = aqa.torria.rangiffler.model.FriendshipStatus.ACCEPTED and f.requester = :requester" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
    Page<UserEntity> findFriends(@Param("requester") UserEntity requester,
                                  @Nonnull Pageable pageable,
                                  @Param("searchQuery") String searchQuery);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
            " where f.status = aqa.torria.rangiffler.model.FriendshipStatus.ACCEPTED and f.requester = :requester")
    List<UserEntity> findFriends(@Param("requester") UserEntity requester);

    // ===== Исходящие приглашения (PENDING, requester = текущий пользователь) =====
    @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
            " where f.status = aqa.torria.rangiffler.model.FriendshipStatus.PENDING and f.requester = :requester")
    Page<UserEntity> findOutcomeInvitations(@Param("requester") UserEntity requester,
                                             @Nonnull Pageable pageable);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
            " where f.status = aqa.torria.rangiffler.model.FriendshipStatus.PENDING and f.requester = :requester" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
    Page<UserEntity> findOutcomeInvitations(@Param("requester") UserEntity requester,
                                             @Nonnull Pageable pageable,
                                             @Param("searchQuery") String searchQuery);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.addressee" +
            " where f.status = aqa.torria.rangiffler.model.FriendshipStatus.PENDING and f.requester = :requester")
    List<UserEntity> findOutcomeInvitations(@Param("requester") UserEntity requester);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.requester" +
            " where f.status = aqa.torria.rangiffler.model.FriendshipStatus.PENDING and f.addressee = :addressee")
    Page<UserEntity> findIncomeInvitations(@Param("addressee") UserEntity addressee,
                                            @Nonnull Pageable pageable);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.requester" +
            " where f.status = aqa.torria.rangiffler.model.FriendshipStatus.PENDING and f.addressee = :addressee" +
            " and (u.username like %:searchQuery% or u.firstname like %:searchQuery% or u.lastName like %:searchQuery%)")
    Page<UserEntity> findIncomeInvitations(@Param("addressee") UserEntity addressee,
                                            @Nonnull Pageable pageable,
                                            @Param("searchQuery") String searchQuery);

    @Query("select u from UserEntity u join FriendshipEntity f on u = f.requester" +
            " where f.status = aqa.torria.rangiffler.model.FriendshipStatus.PENDING and f.addressee = :addressee")
    List<UserEntity> findIncomeInvitations(@Param("addressee") UserEntity addressee);

    @Modifying
    @Query("update FriendshipEntity f set f.status = :status where f.requester = :requester and f.addressee = :addressee")
    void updateFriendshipStatus(@Param("requester") UserEntity requester,
                                @Param("addressee") UserEntity addressee,
                                @Param("status") FriendshipStatus status);

    @Modifying
    @Query("delete from FriendshipEntity f where (f.requester = :user1 and f.addressee = :user2) or (f.requester = :user2 and f.addressee = :user1)")
    void deleteFriendship(@Param("user1") UserEntity user1,
                          @Param("user2") UserEntity user2);

}
