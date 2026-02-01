package aqa.torria.rangiffler.controller.query;

import aqa.torria.rangiffler.model.User;
import aqa.torria.rangiffler.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

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

}
