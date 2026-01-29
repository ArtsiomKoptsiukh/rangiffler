/*
package aqa.torria.rangiffler.controller.mutation;

import aqa.torria.rangiffler.model.User;
import aqa.torria.rangiffler.service.UserService;
import aqa.torria.rangiffler.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;

@Controller
@PreAuthorize("isAuthenticated()")
public class UserMutationController {

    private final UserService userService;

    @Autowired
    public UserMutationController(UserService userService) {
        this.userService = userService;
    }

    @MutationMapping
    public User user(@Argument("input") User input,
                     @AuthenticationPrincipal Jwt jwt) {
        String username = JwtUtil.extractUsername(jwt);
        return userService.updateUser(username, input);
    }
}
*/
