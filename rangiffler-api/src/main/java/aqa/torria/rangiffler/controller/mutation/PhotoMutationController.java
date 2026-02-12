package aqa.torria.rangiffler.controller.mutation;

import aqa.torria.rangiffler.model.Photo;
import aqa.torria.rangiffler.model.PhotoInput;
import aqa.torria.rangiffler.service.PhotoService;
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
public class PhotoMutationController {

    PhotoService photoService;

    @MutationMapping(name = "photo")
    public Photo photo(@AuthenticationPrincipal Jwt principal,
                       @Argument("input") @Valid PhotoInput photoInput) {
        String username = Objects.requireNonNull(principal.getClaimAsString("sub"), "Sub is missing in JWT");

        return photoService.createOrUpdatePhoto(username, photoInput);
    }

    @MutationMapping(name = "deletePhoto")
    public Boolean deletePhoto(@AuthenticationPrincipal Jwt principal,
                               @Argument String id) {
        String username = Objects.requireNonNull(principal.getClaimAsString("sub"), "Sub is missing in JWT");

        return photoService.deletePhoto(username, id);
    }
}
