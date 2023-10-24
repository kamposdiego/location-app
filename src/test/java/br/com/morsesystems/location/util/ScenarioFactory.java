package br.com.morsesystems.location.util;

import br.com.morsesystems.location.adapter.out.persistence.jpa.CountryJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

public class ScenarioFactory {

    public static final CountryJpaEntity COUNTRY_ENTITY_BRAZIL = CountryJpaEntity.builder()
            .id(1L)
            .countryName("Brazil")
            .telephoneCodArea(55)
            .build();

    public static final CountryJpaEntity COUNTRY_ENTITY_UNITED_STATES = CountryJpaEntity.builder()
            .id(2L)
                    .countryName("United States")
                    .telephoneCodArea(1)
                    .build();

    public static final Optional<CountryJpaEntity> OPTIONAL_COUNTRY_ENTITY_BRAZIL = Optional.of(COUNTRY_ENTITY_BRAZIL);

    public static final Page<CountryJpaEntity> COUNTRIES_ENTITY = new PageImpl<>(Arrays.asList(
            CountryJpaEntity.builder()
                    .id(1L)
                    .countryName("Brazil")
                    .telephoneCodArea(55)
                    .build(),
            CountryJpaEntity.builder()
                    .id(2L)
                    .countryName("United States")
                    .telephoneCodArea(1)
                    .build(),
            CountryJpaEntity.builder()
                    .id(3L)
                    .countryName("Japan")
                    .telephoneCodArea(81)
                    .build()), Pageable.unpaged(), 3);

    public static final Page<CountryJpaEntity> COUNTRY_ENTITY_PAGE_WITH_BRAZIL = new PageImpl<>(Arrays.asList(
            CountryJpaEntity.builder()
                    .id(1L)
                    .countryName("Brazil")
                    .telephoneCodArea(55)
                    .build()), Pageable.unpaged(), 1);
}
