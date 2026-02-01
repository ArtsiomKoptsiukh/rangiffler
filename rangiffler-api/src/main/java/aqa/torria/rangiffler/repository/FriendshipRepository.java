package aqa.torria.rangiffler.repository;

import aqa.torria.rangiffler.entity.FriendshipEntity;
import aqa.torria.rangiffler.entity.keys.FriendshipId;
import aqa.torria.rangiffler.model.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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

}
