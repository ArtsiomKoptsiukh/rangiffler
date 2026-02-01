package aqa.torria.rangiffler.service;

import aqa.torria.rangiffler.entity.UserEntity;
import aqa.torria.rangiffler.mappers.UserMapper;
import aqa.torria.rangiffler.model.User;
import aqa.torria.rangiffler.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserMapper userMapper;

    public User getCurrentUser(String username) {
        UserEntity entity = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return userMapper.toUser(entity);
    }

    /*@Transactional
    public User updateUser(String username, User user) {
        UserEntity existingEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        // Обновляем только те поля, которые пришли
        if (user.getFirstname() != null) {
            existingEntity.setFirstname(user.getFirstname());
        }
        if (user.getSurname() != null) {
            existingEntity.setLastName(user.getSurname());
        }
        if (user.getAvatar() != null) {
            existingEntity.setAvatar(userMapper.decodeAvatar(user.getAvatar()));
        }
        if (user.getLocation() != null) {
            existingEntity.setCountry(userMapper.toCountryEntity(user.getLocation()));
        }

        UserEntity savedEntity = userRepository.save(existingEntity);
        return userMapper.toUser(savedEntity);
    }*/
}
