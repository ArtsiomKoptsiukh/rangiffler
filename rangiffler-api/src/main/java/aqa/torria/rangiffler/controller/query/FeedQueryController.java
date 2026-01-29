package aqa.torria.rangiffler.controller.query;

import aqa.torria.rangiffler.model.Feed;
import aqa.torria.rangiffler.model.Photo;
import aqa.torria.rangiffler.model.Stat;
import aqa.torria.rangiffler.service.FeedService;
import aqa.torria.rangiffler.util.GqlQueryPaginationAndSort;
import io.micrometer.common.lang.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
@PreAuthorize("isAuthenticated()")
public class FeedQueryController {

    private final FeedService feedService;

    @Autowired
    public FeedQueryController(FeedService feedService) {
        this.feedService = feedService;
    }

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
        return List.of();
    }

    @QueryMapping
    public Feed feed(@Argument boolean withFriends,
                     @AuthenticationPrincipal Jwt principal) {

        return Feed.builder()
                .username(principal.getClaimAsString("sub"))
                .withFriends(withFriends)
                .build();
    }
}
