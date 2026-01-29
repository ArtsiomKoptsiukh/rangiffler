package aqa.torria.rangiffler.mappers;

import aqa.torria.rangiffler.entity.CountryEntity;
import aqa.torria.rangiffler.entity.PhotoEntity;
import aqa.torria.rangiffler.model.Country;
import aqa.torria.rangiffler.model.Photo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CountryMapper.class)
public interface PhotoMapper {

    @Mapping(target = "id", expression = "java(entity.getId().toString())")
    @Mapping(target = "src", expression = "java(bytesToString(entity.getPhoto()))")
    @Mapping(target = "country", source = "country")
    @Mapping(target = "creationDate", expression = "java(toDate(entity.getCreatedDate()))")
    @Mapping(target = "likes", ignore = true)
    @Mapping(target = "isOwner", ignore = true)
    Photo toPhoto(PhotoEntity entity);

    @Mapping(target = "id", expression = "java(photo.getId() != null ? java.util.UUID.fromString(photo.getId()) : null)")
    @Mapping(target = "photo", expression = "java(stringToBytes(photo.getSrc()))")
    @Mapping(target = "country", source = "country")
    @Mapping(target = "createdDate", expression = "java(toOffsetDateTime(photo.getCreationDate()))")
    @Mapping(target = "userId", ignore = true)
    PhotoEntity toPhotoEntity(Photo photo);

    default String bytesToString(byte[] bytes) {
        return bytes != null ? new String(bytes, java.nio.charset.StandardCharsets.UTF_8) : null;
    }

    default byte[] stringToBytes(String s) {
        return s != null ? s.getBytes(java.nio.charset.StandardCharsets.UTF_8) : null;
    }

    default java.util.Date toDate(java.time.OffsetDateTime odt) {
        return odt != null ? java.util.Date.from(odt.toInstant()) : null;
    }

    default java.time.OffsetDateTime toOffsetDateTime(java.util.Date date) {
        return date != null
                ? java.time.OffsetDateTime.ofInstant(date.toInstant(), java.time.ZoneOffset.UTC)
                : null;
    }
}
