package aqa.torria.rangiffler.service;

import aqa.torria.rangiffler.mappers.PhotoMapper;
import aqa.torria.rangiffler.model.Photo;
import aqa.torria.rangiffler.repository.PhotoRepository;
import aqa.torria.rangiffler.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final PhotoMapper photoMapper;

    public Page<Photo> loadFeedPhotos(String username, boolean withFriends, Pageable pageable) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        return photoRepository.findByUserId(user.getId(), pageable)
                .map(photoMapper::toPhoto);
    }
}
