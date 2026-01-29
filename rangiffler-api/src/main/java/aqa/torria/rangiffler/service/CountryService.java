package aqa.torria.rangiffler.service;

import aqa.torria.rangiffler.entity.CountryEntity;
import aqa.torria.rangiffler.mappers.CountryMapper;
import aqa.torria.rangiffler.model.Country;
import aqa.torria.rangiffler.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Autowired
    public CountryService(CountryRepository countryRepository, CountryMapper countryMapper) {
        this.countryRepository = countryRepository;
        this.countryMapper = countryMapper;
    }

    public List<Country> getAllCountries() {
        List<CountryEntity> entities = countryRepository.findAll();
        return entities.stream()
                .map(countryMapper::toCountry)
                .toList();
    }
}
