package aqa.torria.rangiffler.controller.mutation;

import aqa.torria.rangiffler.model.FriendshipInput;
import aqa.torria.rangiffler.model.User;
import aqa.torria.rangiffler.model.UserInput;
import aqa.torria.rangiffler.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

import java.util.Objects;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserMutationController {

    UserService userService;

    @MutationMapping(name = "user")
    public User user(@Argument("input") @Valid UserInput userInput,
                     @AuthenticationPrincipal Jwt principal) {
        String username = Objects.requireNonNull(principal.getClaimAsString("sub"), "Sub is missing in JWT");

        return userService.updateProfile(username, userInput);
    }

    @MutationMapping(name = "friendship")
    public User friendship(@AuthenticationPrincipal Jwt principal,
                           @Argument @Valid FriendshipInput friendshipInput) {
        String username = Objects.requireNonNull(principal.getClaimAsString("sub"), "Sub is missing in JWT");

        return userService.updateFriendship(username, friendshipInput);
    }
}
