package br.com.morsesystems.location.adapter;

import br.com.morsesystems.location.domain.Country;
import br.com.morsesystems.location.application.port.CountrySavePort;
import br.com.morsesystems.location.application.repository.entity.CountryEntity;
import br.com.morsesystems.location.application.repository.CountryJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CountryJPAPersistenceAdapter implements CountrySavePort {

    private final CountryJpaRepository countryJpaRepository;

    @Override
    public Country save(Country country) {

        log.info(String.format("Country with name %s and DDI %s will be saved.",
                country.getCountryName(),
                country.getTelephoneCodArea()));

        CountryEntity value = countryJpaRepository.save(CountryEntity
                .builder()
                .countryName(country.getCountryName())
                .telephoneCodArea(country.getTelephoneCodArea())
                .build());

        log.info(String.format("Country is saved with name %s and DDI %s, the ID is %s.",
                                value.getCountryName(),
                value.getTelephoneCodArea(), value.getId()));

        return Country.builder()
                .id(value.getId())
                .countryName(value.getCountryName())
                .telephoneCodArea(value.getTelephoneCodArea())
                .build();
    }

}