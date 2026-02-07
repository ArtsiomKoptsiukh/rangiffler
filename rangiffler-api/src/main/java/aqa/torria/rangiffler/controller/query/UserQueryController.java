package aqa.torria.rangiffler.controller.query;

import aqa.torria.rangiffler.entity.UserEntity;
import aqa.torria.rangiffler.model.User;
import aqa.torria.rangiffler.service.UserService;
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

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserQueryController {

    UserService userService;

    @QueryMapping
    public User user(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaim("sub");
        return userService.getCurrentUser(username);
    }

    @SchemaMapping(typeName = "User", field = "friends")
    public Page<User> friends(User user,
                              @Argument Integer page,
                              @Argument Integer size,
                              @Argument @Nullable String searchQuery) {
        Pageable pageable = new GqlQueryPaginationAndSort(page, size, List.of()).pageable();
        UserEntity userEntity = userService.getUserEntityByUsername(user.getUsername());
        return userService.getFriends(userEntity, pageable, searchQuery);
    }

    @SchemaMapping(typeName = "User", field = "incomeInvitations")
    public Page<User> incomeInvitations(User user,
                                        @Argument Integer page,
                                        @Argument Integer size,
                                        @Argument @Nullable String searchQuery) {
        Pageable pageable = new GqlQueryPaginationAndSort(page, size, List.of()).pageable();
        UserEntity userEntity = userService.getUserEntityByUsername(user.getUsername());
        return userService.getIncomeInvitations(userEntity, pageable, searchQuery);
    }

    @SchemaMapping(typeName = "User", field = "outcomeInvitations")
    public Page<User> outcomeInvitations(User user,
                                         @Argument Integer page,
                                         @Argument Integer size,
                                         @Argument @Nullable String searchQuery) {
        Pageable pageable = new GqlQueryPaginationAndSort(page, size, List.of()).pageable();
        UserEntity userEntity = userService.getUserEntityByUsername(user.getUsername());
        return userService.getOutcomeInvitations(userEntity, pageable, searchQuery);
    }
}
