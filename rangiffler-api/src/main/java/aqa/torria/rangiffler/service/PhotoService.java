package aqa.torria.rangiffler.service;

import aqa.torria.rangiffler.entity.CountryEntity;
import aqa.torria.rangiffler.entity.LikeEntity;
import aqa.torria.rangiffler.entity.PhotoEntity;
import aqa.torria.rangiffler.entity.UserEntity;
import aqa.torria.rangiffler.mappers.PhotoMapper;
import aqa.torria.rangiffler.model.CountryInput;
import aqa.torria.rangiffler.model.Photo;
import aqa.torria.rangiffler.model.PhotoInput;
import aqa.torria.rangiffler.repository.CountryRepository;
import aqa.torria.rangiffler.repository.LikeRepository;
import aqa.torria.rangiffler.repository.PhotoRepository;
import aqa.torria.rangiffler.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PhotoService {

    PhotoRepository photoRepository;
    CountryRepository countryRepository;
    UserRepository userRepository;
    LikeRepository likeRepository;
    PhotoMapper photoMapper;

    @Transactional
    public Photo createOrUpdatePhoto(String username, PhotoInput input) {
        UserEntity user = getUser(username);
        PhotoEntity photoEntity = input.getId() != null
                ? updatePhoto(input, user)
                : createPhoto(input, user);
        PhotoEntity saved = photoRepository.save(photoEntity);
        return photoMapper.toPhoto(saved, user.getId());
    }

    @Transactional
    public boolean deletePhoto(String username, String photoId) {
        UserEntity user = getUser(username);
        PhotoEntity photo = getPhotoById(photoId);
        verifyOwnership(photo, user);
        photoRepository.delete(photo);
        return true;
    }

    private PhotoEntity createPhoto(PhotoInput input, UserEntity user) {
        Objects.requireNonNull(input.getSrc(), "Photo src is required for new photos");
        Objects.requireNonNull(input.getCountry(), "Country is required for new photos");
        Objects.requireNonNull(input.getCountry().getCode(), "Country code is required for new photos");

        return PhotoEntity.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .photo(input.getSrc().getBytes(StandardCharsets.UTF_8))
                .country(getCountry(input.getCountry().getCode()))
                .description(Optional.ofNullable(input.getDescription()).orElse(""))
                .createdDate(OffsetDateTime.now())
                .build();
    }

    private PhotoEntity updatePhoto(PhotoInput input, UserEntity user) {
        PhotoEntity photo = getPhotoById(input.getId());
        verifyOwnership(photo, user);

        Optional.ofNullable(input.getDescription()).ifPresent(photo::setDescription);
        Optional.ofNullable(input.getCountry())
                .map(CountryInput::getCode)
                .map(this::getCountry)
                .ifPresent(photo::setCountry);
        Optional.ofNullable(input.getSrc())
                .map(src -> src.getBytes(StandardCharsets.UTF_8))
                .ifPresent(photo::setPhoto);
        Optional.ofNullable(input.getLike())
                .ifPresent(like -> handleLike(photo, user));

        return photo;
    }

    private void handleLike(PhotoEntity photo, UserEntity user) {
        LikeEntity like = LikeEntity.builder()
                .id(UUID.randomUUID())
                .userId(user.getId())
                .createdDate(OffsetDateTime.now())
                .build();
        likeRepository.save(like);
        likeRepository.addLikeToPhoto(photo.getId(), like.getId());
    }

    private UserEntity getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    private PhotoEntity getPhotoById(String photoId) {
        return photoRepository.findById(UUID.fromString(photoId))
                .orElseThrow(() -> new IllegalArgumentException("Photo not found: " + photoId));
    }

    private CountryEntity getCountry(String code) {
        return countryRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Country not found: " + code));
    }

    private void verifyOwnership(PhotoEntity photo, UserEntity user) {
        if (!photo.getUserId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only modify your own photos");
        }
    }
}
