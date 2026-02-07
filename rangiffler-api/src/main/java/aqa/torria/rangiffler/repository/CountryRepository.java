package aqa.torria.rangiffler.repository;

import aqa.torria.rangiffler.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CountryRepository extends JpaRepository<CountryEntity, UUID> {

    List<CountryEntity> findAll();

    Optional<CountryEntity> findByCode(String code);
}
