package br.com.morsesystems.location.adapter.out.persistence.jpa;

import br.com.morsesystems.location.application.exception.InvalidFilterParameterException;
import br.com.morsesystems.location.application.exception.NotFoundException;
import br.com.morsesystems.location.application.port.out.DeleteCountryPort;
import br.com.morsesystems.location.application.port.out.GetCountryPort;
import br.com.morsesystems.location.application.port.out.UpdateCountryPort;
import br.com.morsesystems.location.domain.Country;
import br.com.morsesystems.location.application.port.out.SaveCountryPort;
import br.com.morsesystems.location.application.util.ExceptionFormatterUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
class CountryJPAPersistenceAdapter implements GetCountryPort, SaveCountryPort, UpdateCountryPort, DeleteCountryPort {

    private final CountryJpaRepository countryJpaRepository;
    private final SpecifcationFactory specifcationFactory;
    private final ModelMapper modelMapper;

    private CountryJpaEntity convertToEntity(Country Country) {
        return modelMapper.map(Country, CountryJpaEntity.class);
    }

    private Country convertToDto(CountryJpaEntity countryJpaEntity) {
        return modelMapper.map(countryJpaEntity, Country.class);
    }

    @Override
    public Country get(Long countryId) {
        log.info(String.format("Country with id %s is in search.", countryId));

        return this.convertToDto(countryJpaRepository
                .findById(countryId)
                .orElseThrow(() -> new NotFoundException("Country not found.")));
    }

    @Override
    public Page<Country> getCountries(Pageable pageable, String filter) {
        try {
            log.info(String.format("Search by countries was performed."));

            Page<CountryJpaEntity> countries = countryJpaRepository.findAll(specifcationFactory
                    .createSpecification(CountryJpaEntity.class, filter), pageable);

            List<Country> countriesConverted = countries.getContent().stream().map(this::convertToDto).collect(Collectors.toList());

            return new PageImpl<>(countriesConverted, pageable, countries.getTotalElements());

        } catch (InvalidDataAccessApiUsageException invalidDataAccessApiUsageException){
            throw new InvalidFilterParameterException(ExceptionFormatterUtil
                    .semanticExceptionFormatter(invalidDataAccessApiUsageException.getMessage()));
        }
    }

    @Override
    public Country save(Country country) {

        log.info(String.format("Country with name %s and DDI %s will be saved.",
                country.getCountryName(),
                country.getTelephoneCodArea()));

        CountryJpaEntity value = countryJpaRepository.save(this.convertToEntity(country));

        log.info(String.format("Country is saved with name %s and DDI %s, the ID is %s.",
                value.getCountryName(),
                value.getTelephoneCodArea(), value.getId()));

        return this.convertToDto(value);

    }

    @Override
    public Country update(Country country) {
        log.info(String.format("Country with name %s and DDI %s will be saved.",
                country.getCountryName(),
                country.getTelephoneCodArea()));

        countryJpaRepository.findById(country.getId()).orElseThrow(() -> new NotFoundException("Country not found."));

        CountryJpaEntity value = countryJpaRepository.save(this.convertToEntity(country));

        log.info(String.format("Country is saved with name %s and DDI %s, the ID is %s.",
                value.getCountryName(),
                value.getTelephoneCodArea(), value.getId()));

        return this.convertToDto(value);
    }


    @Override
    public void delete(Long countryId) {
        log.info(String.format("Country with id %s will be deleted.", countryId));

        countryJpaRepository.findById(countryId).orElseThrow(() -> new NotFoundException("Country not found."));

        countryJpaRepository.deleteById(countryId);

        log.info(String.format("Country with id %s was deleted.", countryId));
    }

}