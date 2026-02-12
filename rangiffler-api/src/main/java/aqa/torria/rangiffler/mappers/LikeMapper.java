package aqa.torria.rangiffler.mappers;

import aqa.torria.rangiffler.model.Like;
import aqa.torria.rangiffler.view.PhotoLikeView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.OffsetDateTime;
import java.util.Date;

@Mapper(componentModel = "spring")
public interface LikeMapper {

    @Mapping(target = "user", expression = "java(view.getUserId().toString())")
    @Mapping(target = "username", source = "username")
    @Mapping(target = "creationDate", expression = "java(toDate(view.getCreatedDate()))")
    Like toLike(PhotoLikeView view);

    default Date toDate(OffsetDateTime odt) {
        return odt != null ? Date.from(odt.toInstant()) : null;
    }
}
