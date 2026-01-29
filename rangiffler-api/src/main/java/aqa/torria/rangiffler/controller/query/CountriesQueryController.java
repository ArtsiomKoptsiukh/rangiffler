package aqa.torria.rangiffler.controller.query;

import aqa.torria.rangiffler.model.Country;
import aqa.torria.rangiffler.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@PreAuthorize("isAuthenticated()")
public class CountriesQueryController {

    CountryService countryService;

    @Autowired
    public CountriesQueryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @QueryMapping
    public List<Country> countries() {
        return countryService.getAllCountries();
    }

}
