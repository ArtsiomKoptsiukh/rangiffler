package aqa.torria.rangiffler.service;

import aqa.torria.rangiffler.entity.CountryEntity;
import aqa.torria.rangiffler.entity.FriendshipEntity;
import aqa.torria.rangiffler.entity.UserEntity;
import aqa.torria.rangiffler.mappers.UserMapper;
import aqa.torria.rangiffler.model.FriendshipInput;
import aqa.torria.rangiffler.model.FriendshipStatus;
import aqa.torria.rangiffler.model.FriendStatus;
import aqa.torria.rangiffler.model.User;
import aqa.torria.rangiffler.model.UserInput;
import aqa.torria.rangiffler.repository.CountryRepository;
import aqa.torria.rangiffler.repository.FriendshipRepository;
import aqa.torria.rangiffler.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    CountryRepository countryRepository;

    UserMapper userMapper;
    FriendshipRepository friendshipRepository;

    public User getCurrentUser(String username) {
        UserEntity entity = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return userMapper.toUser(entity);
    }

    public Page<User> getFriends(UserEntity user, Pageable pageable, String searchQuery) {
        Page<UserEntity> slice = (searchQuery == null || searchQuery.isBlank())
                ? friendshipRepository.findFriends(user, pageable)
                : friendshipRepository.findFriends(user, pageable, searchQuery);

        List<User> users = slice.getContent().stream()
                .map(userMapper::toUser)
                .toList();

        return new PageImpl<>(users, pageable, users.size());
    }

    public Page<User> getIncomeInvitations(UserEntity user, Pageable pageable, String searchQuery) {
        Page<UserEntity> slice = (searchQuery == null || searchQuery.isBlank())
                ? friendshipRepository.findIncomeInvitations(user, pageable)
                : friendshipRepository.findIncomeInvitations(user, pageable, searchQuery);

        List<User> users = slice.getContent().stream()
                .map(userMapper::toUser)
                .toList();

        return new PageImpl<>(users, pageable, users.size());
    }

    public Page<User> getOutcomeInvitations(UserEntity user, Pageable pageable, String searchQuery) {
        Page<UserEntity> slice = (searchQuery == null || searchQuery.isBlank())
                ? friendshipRepository.findOutcomeInvitations(user, pageable)
                : friendshipRepository.findOutcomeInvitations(user, pageable, searchQuery);

        List<User> users = slice.getContent().stream()
                .map(userMapper::toUser)
                .toList();

        return new PageImpl<>(users, pageable, users.size());
    }

    public UserEntity getUserEntityByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public Page<User> getAllUsers(String currentUsername, Pageable pageable, String searchQuery) {
        UserEntity currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + currentUsername));

        Page<UserEntity> page = (searchQuery == null || searchQuery.isBlank())
                ? userRepository.findAllExceptCurrent(currentUsername, pageable)
                : userRepository.findAllExceptCurrent(currentUsername, pageable, searchQuery);

        List<User> users = page.getContent().stream()
                .map(userEntity -> {
                    User user = userMapper.toUser(userEntity);
                    user.setFriendStatus(determineFriendStatus(currentUser, userEntity));
                    return user;
                })
                .toList();

        return new PageImpl<>(users, pageable, page.getTotalElements());
    }

    private FriendStatus determineFriendStatus(UserEntity currentUser, UserEntity targetUser) {
        // Проверяем, есть ли дружба от currentUser к targetUser
        Optional<FriendshipEntity> friendshipAsRequester = friendshipRepository.findByRequesterAndAddressee(currentUser, targetUser);
        if (friendshipAsRequester.isPresent()) {
            FriendshipEntity friendship = friendshipAsRequester.get();
            if (friendship.getStatus() == FriendshipStatus.ACCEPTED) {
                return FriendStatus.FRIEND;
            } else if (friendship.getStatus() == FriendshipStatus.PENDING) {
                return FriendStatus.INVITATION_SENT;
            }
        }

        // Проверяем, есть ли дружба от targetUser к currentUser
        Optional<FriendshipEntity> friendshipAsAddressee = friendshipRepository.findByRequesterAndAddressee(targetUser, currentUser);
        if (friendshipAsAddressee.isPresent()) {
            FriendshipEntity friendship = friendshipAsAddressee.get();
            if (friendship.getStatus() == FriendshipStatus.ACCEPTED) {
                return FriendStatus.FRIEND;
            } else if (friendship.getStatus() == FriendshipStatus.PENDING) {
                return FriendStatus.INVITATION_RECEIVED;
            }
        }

        return FriendStatus.NOT_FRIEND;
    }

    @Transactional
    public User updateProfile(String username, UserInput userInput) {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        userMapper.patchUser(userInput, userEntity);

        if (userInput.getLocation() != null && userInput.getLocation().getCode() != null) {
            CountryEntity countryEntity = countryRepository.findByCode(userInput.getLocation().getCode())
                    .orElseThrow(() -> new IllegalArgumentException("Country not found: " + userInput.getLocation().getCode()));
            userEntity.setCountry(countryEntity);
        }

        UserEntity saved = userRepository.save(userEntity);

        return userMapper.toUser(saved);
    }

    @Transactional
    public User updateFriendship(String currentUsername, FriendshipInput input) {
        UserEntity currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUsername));

        UserEntity targetUser = userRepository.findById(UUID.fromString(input.getUser()))
                .orElseThrow(() -> new IllegalArgumentException("Target user not found: " + input.getUser()));

        switch (input.getAction()) {
            case ADD -> sendFriendRequest(currentUser, targetUser);
            case ACCEPT -> acceptFriendRequest(currentUser, targetUser);
            case REJECT -> rejectFriendRequest(currentUser, targetUser);
            case DELETE -> deleteFriend(currentUser, targetUser);
        }

        return userMapper.toUser(targetUser);
    }

    private void sendFriendRequest(UserEntity currentUser, UserEntity targetUser) {
        FriendshipEntity friendship = new FriendshipEntity(
                currentUser,
                targetUser,
                LocalDateTime.now(),
                FriendshipStatus.PENDING
        );
        friendshipRepository.save(friendship);
    }

    private void acceptFriendRequest(UserEntity currentUser, UserEntity targetUser) {
        // targetUser отправил запрос currentUser, поэтому targetUser = requester, currentUser = addressee
        friendshipRepository.updateFriendshipStatus(targetUser, currentUser, FriendshipStatus.ACCEPTED);
    }

    private void rejectFriendRequest(UserEntity currentUser, UserEntity targetUser) {
        // targetUser отправил запрос currentUser, поэтому targetUser = requester, currentUser = addressee
        friendshipRepository.deleteFriendship(targetUser, currentUser);
    }

    private void deleteFriend(UserEntity currentUser, UserEntity targetUser) {
        // Дружба может быть в любом направлении, удаляем в обе стороны
        friendshipRepository.deleteFriendship(currentUser, targetUser);
    }
}
