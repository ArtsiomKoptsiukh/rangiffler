package aqa.torria.rangiffler.mappers;

import aqa.torria.rangiffler.entity.CountryEntity;
import aqa.torria.rangiffler.model.Country;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.nio.charset.StandardCharsets;

@Mapper(componentModel = "spring")
public interface CountryMapper {

    @Mapping(target = "flag", expression = "java(bytesToString(entity.getFlag()))")
    Country toCountry(CountryEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flag", expression = "java(stringToBytes(country.getFlag()))")
    CountryEntity toCountryEntity(Country country);

    default String bytesToString(byte[] bytes) {
        return bytes != null ? new String(bytes, StandardCharsets.UTF_8) : null;
    }

    default byte[] stringToBytes(String s) {
        return s != null ? s.getBytes(StandardCharsets.UTF_8) : null;
    }
}
