package aqa.torria.rangiffler.controller.query;

import aqa.torria.rangiffler.model.Country;
import aqa.torria.rangiffler.service.CountryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CountriesQueryController {

    CountryService countryService;

    @QueryMapping
    public List<Country> countries() {
        return countryService.getAllCountries();
    }

}
