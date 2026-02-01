package aqa.torria.rangiffler.service;

import aqa.torria.rangiffler.entity.UserEntity;
import aqa.torria.rangiffler.mappers.CountryMapper;
import aqa.torria.rangiffler.mappers.PhotoMapper;
import aqa.torria.rangiffler.model.FriendshipStatus;
import aqa.torria.rangiffler.model.Photo;
import aqa.torria.rangiffler.model.Stat;
import aqa.torria.rangiffler.repository.FriendshipRepository;
import aqa.torria.rangiffler.repository.PhotoRepository;
import aqa.torria.rangiffler.repository.UserRepository;
import aqa.torria.rangiffler.repository.projection.CountryStatRow;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedService {

    UserRepository userRepository;
    PhotoRepository photoRepository;
    FriendshipRepository friendshipRepository;
    PhotoMapper photoMapper;
    CountryMapper countryMapper;

    public Page<Photo> loadFeedPhotos(String username, boolean withFriends, Pageable pageable) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        List<UUID> userIds = resolveUserIds(user.getId(), withFriends);

        UUID myId = user.getId();

        return photoRepository.findByUserIdIn(userIds, pageable)
                .map(entity -> photoMapper.toPhoto(entity, myId));
    }

    public List<Stat> loadFeedStat(String username, boolean withFriends) {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        List<UUID> userIds = resolveUserIds(user.getId(), withFriends);
        if (userIds.isEmpty()) {
            return List.of();
        }

        List<CountryStatRow> rows = photoRepository.countByCountry(userIds);

        return rows.stream()
                .map(r -> new Stat(
                        Math.toIntExact(r.getCount()),
                        countryMapper.toCountry(r.getCountry())
                ))
                .toList();
    }

    private List<UUID> resolveUserIds(UUID myId, boolean withFriends) {
        if (!withFriends) {
            return List.of(myId);
        }

        List<UUID> friends = friendshipRepository.findFriendIds(myId, FriendshipStatus.ACCEPTED);

        ArrayList<UUID> ids = new ArrayList<>(friends.size() + 1);
        ids.add(myId);
        ids.addAll(friends);
        return ids;
    }
}
