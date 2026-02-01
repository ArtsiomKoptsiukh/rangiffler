package aqa.torria.rangiffler.repository.projection;

import aqa.torria.rangiffler.entity.CountryEntity;

public interface CountryStatRow {
    CountryEntity getCountry();
    long getCount();
}
