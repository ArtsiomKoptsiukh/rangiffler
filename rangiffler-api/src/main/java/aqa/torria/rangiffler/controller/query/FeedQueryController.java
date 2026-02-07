package aqa.torria.rangiffler.controller.query;

import aqa.torria.rangiffler.model.Feed;
import aqa.torria.rangiffler.model.Likes;
import aqa.torria.rangiffler.model.Photo;
import aqa.torria.rangiffler.model.Stat;
import aqa.torria.rangiffler.service.FeedService;
import aqa.torria.rangiffler.service.LikeService;
import aqa.torria.rangiffler.util.GqlQueryPaginationAndSort;
import io.micrometer.common.lang.Nullable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FeedQueryController {

    FeedService feedService;
    LikeService likeService;

    @SchemaMapping(typeName = "Feed", field = "photos")
    public Page<Photo> photos(Feed feed,
                              @Argument Integer page,
                              @Argument Integer size,
                              @Argument @Nullable List<String> sort) {

        Pageable pageable = new GqlQueryPaginationAndSort(page, size, sort).pageable();

        return feedService.loadFeedPhotos(feed.getUsername(), feed.getWithFriends(), pageable);
    }

    @SchemaMapping(typeName = "Feed", field = "stat")
    public List<Stat> stat(Feed feed) {
        return feedService.loadFeedStat(feed.getUsername(), feed.getWithFriends());
    }

    @SchemaMapping(typeName = "Photo", field = "likes")
    public Likes likes(Photo photo) {
        if (photo == null || photo.getId() == null) {
            Likes empty = new Likes();
            empty.setTotal(0);
            empty.setLikes(List.of());
            return empty;
        }
        UUID photoId = UUID.fromString(photo.getId());
        return likeService.getLikesForPhoto(photoId);
    }

    @QueryMapping
    public Feed feed(@Argument boolean withFriends,
                     @AuthenticationPrincipal Jwt principal) {
        String username = Objects.requireNonNull(principal.getClaimAsString("sub"), "sub is missing");

        return Feed.builder()
                .username(username)
                .withFriends(withFriends)
                .build();
    }
}
