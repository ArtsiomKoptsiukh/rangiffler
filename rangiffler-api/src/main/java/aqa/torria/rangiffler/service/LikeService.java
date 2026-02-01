package aqa.torria.rangiffler.service;

import aqa.torria.rangiffler.mappers.LikeMapper;
import aqa.torria.rangiffler.model.Like;
import aqa.torria.rangiffler.model.Likes;
import aqa.torria.rangiffler.repository.LikeRepository;
import aqa.torria.rangiffler.view.PhotoLikeView;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LikeService {

    LikeRepository likeRepository;
    LikeMapper likeMapper;

    public Likes getLikesForPhoto(UUID photoId) {
        long total = likeRepository.countLikesByPhotoId(photoId);

        List<PhotoLikeView> rows = total == 0
                ? List.of()
                : likeRepository.findLikesByPhotoId(photoId);

        List<Like> likes = rows.stream()
                .map(likeMapper::toLike)
                .toList();

        Likes result = new Likes();
        result.setTotal(Math.toIntExact(total));
        result.setLikes(likes);
        return result;
    }
}

