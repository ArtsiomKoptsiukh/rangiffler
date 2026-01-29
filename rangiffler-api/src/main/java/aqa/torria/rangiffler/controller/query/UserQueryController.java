package aqa.torria.rangiffler.controller.query;

import aqa.torria.rangiffler.model.User;
import aqa.torria.rangiffler.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

@Controller
@PreAuthorize("isAuthenticated()")
public class UserQueryController {

    private final UserService userService;

    @Autowired
    public UserQueryController(UserService userService) {
        this.userService = userService;
    }

    @QueryMapping
    public User user(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaim("sub");
        return userService.getCurrentUser(username);
    }

}
