package aqa.torria.rangiffler.mappers;

import aqa.torria.rangiffler.entity.UserEntity;
import aqa.torria.rangiffler.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CountryMapper.class)
public interface UserMapper {

    @Mapping(target = "id", expression = "java(entity.getId().toString())")
    @Mapping(target = "surname", source = "lastName")
    @Mapping(target = "avatar", expression = "java(bytesToString(entity.getAvatar()))")
    @Mapping(target = "location", source = "country")
    @Mapping(target = "friendStatus", ignore = true)
    @Mapping(target = "friends", ignore = true)
    @Mapping(target = "incomeInvitations", ignore = true)
    @Mapping(target = "outcomeInvitations", ignore = true)
    User toUser(UserEntity entity);

    @Mapping(target = "id", expression = "java(user.getId() != null ? java.util.UUID.fromString(user.getId()) : null)")
    @Mapping(target = "lastName", source = "surname")
    @Mapping(target = "avatar", expression = "java(stringToBytes(user.getAvatar()))")
    @Mapping(target = "country", source = "location")
    UserEntity toUserEntity(User user);

    default String bytesToString(byte[] bytes) {
        return bytes != null ? new String(bytes, java.nio.charset.StandardCharsets.UTF_8) : null;
    }

    default byte[] stringToBytes(String s) {
        return s != null ? s.getBytes(java.nio.charset.StandardCharsets.UTF_8) : null;
    }
}
