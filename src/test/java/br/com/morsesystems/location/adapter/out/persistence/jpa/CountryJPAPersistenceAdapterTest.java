package br.com.morsesystems.location.adapter.out.persistence.jpa;

import br.com.morsesystems.location.adapter.out.persistence.jpa.CountryJPAPersistenceAdapter;
import br.com.morsesystems.location.application.exception.InvalidFilterParameterException;
import br.com.morsesystems.location.application.exception.NotFoundException;
import br.com.morsesystems.location.adapter.out.persistence.jpa.CountryJpaRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.morsesystems.location.adapter.out.persistence.jpa.CountryEntity;
import br.com.morsesystems.location.domain.Country;
import br.com.morsesystems.location.adapter.out.persistence.jpa.SpecifcationFactory;
import br.com.morsesystems.location.util.ScenarioFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(SpringExtension.class)
public class CountryJPAPersistenceAdapterTest {

    @MockBean
    private CountryJpaRepository countryJpaRepository;

    @MockBean
    private SpecifcationFactory specifcationFactory;

    @SpyBean
    private ModelMapper modelMapper;

    @SpyBean
    private CountryJPAPersistenceAdapter countryJPAPersistenceAdapter;

    @Test
    void get_whenCountryIdIsNotNull_shouldReturnCountry() {
        given(countryJpaRepository.findById(anyLong())).willReturn(Optional.of(CountryEntity.builder()
                        .id(1L)
                        .countryName("Brazil")
                        .telephoneCodArea(55)
                        .build()));

        Country country = countryJPAPersistenceAdapter.get(1L);

        then(countryJpaRepository).should(times(1)).findById(anyLong());
        assertEquals(1L, country.getId());
        assertEquals("Brazil", country.getCountryName());
        assertEquals(55, country.getTelephoneCodArea());
    }

    @Test
    void get_whenCountryIdIsNull_shouldThrowNotFoundException() {
        given(countryJpaRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> countryJPAPersistenceAdapter.get(1L));
        then(countryJpaRepository).should(times(1)).findById(anyLong());
    }

    @Test
    void getCountries_whenFilterIsNull_shouldReturnCountries() {
        given(specifcationFactory.createSpecification(Country.class, null)).willReturn(null);
        given(countryJpaRepository.findAll((Specification<CountryEntity>) eq(null), any(Pageable.class)))
                .willReturn(ScenarioFactory.COUNTRIES_ENTITY);

        Page<Country> countries = countryJPAPersistenceAdapter.getCountries(Pageable.unpaged(), null);

        assertEquals(3, countries.getTotalElements());
        assertEquals(1L, countries.getContent().get(0).getId());
        assertEquals("Brazil", countries.getContent().get(0).getCountryName());
        assertEquals(55, countries.getContent().get(0).getTelephoneCodArea());

        then(countryJpaRepository).should(times(1)).findAll((Specification<CountryEntity>) eq(null), any(Pageable.class));
    }

    @Test
    void getCountries_whenFilterIsCountryName_shouldReturnOneCountry(){
        given(specifcationFactory.createSpecification(any(), anyString())).willReturn(Specification.anyOf());
        given(countryJpaRepository.findAll(any(Specification.class), any(Pageable.class)))
                .willReturn(ScenarioFactory.COUNTRY_ENTITY_PAGE_WITH_BRAZIL);

        Page<Country> countries = countryJPAPersistenceAdapter.getCountries(Pageable.unpaged(), "countryName:Brazil");

        assertEquals(1, countries.getTotalElements());
        assertEquals(1L, countries.getContent().get(0).getId());
        assertEquals("Brazil", countries.getContent().get(0).getCountryName());
        assertEquals(55, countries.getContent().get(0).getTelephoneCodArea());

        then(specifcationFactory).should(times(1)).createSpecification(any(), anyString());
        then(countryJpaRepository).should(times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void getCountries_whenFilterIsInvalid_shouldThrowInvalidDataAccessApiUsageException(){
        given(specifcationFactory.createSpecification(any(), anyString())).willReturn(Specification.anyOf());
        given(countryJpaRepository.findAll(any(Specification.class), any(Pageable.class)))
                .willThrow(new InvalidDataAccessApiUsageException("SemanticException: org.hibernate.query.SemanticException: Could not resolve attribute 'field' of 'br.com.morsesystems.location.adapter.out.persistence.jpa.CountryEntity'"));

        assertThrows(InvalidFilterParameterException.class, () -> countryJPAPersistenceAdapter.getCountries(Pageable.unpaged(), "field:razil"));


        then(specifcationFactory).should(times(1)).createSpecification(any(), anyString());
        then(countryJpaRepository).should(times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void getCountries_whenFilterIsInvalid_shouldThrowInvalidDataAccessApiUsageExceptionWithAnotherMessage(){
        given(specifcationFactory.createSpecification(any(), anyString())).willReturn(Specification.anyOf());
        given(countryJpaRepository.findAll(any(Specification.class), any(Pageable.class)))
                .willThrow(new InvalidDataAccessApiUsageException("RuntimeException:  another message'"));

        assertThrows(InvalidFilterParameterException.class, () -> countryJPAPersistenceAdapter.getCountries(Pageable.unpaged(), "field:razil"));


        then(specifcationFactory).should(times(1)).createSpecification(any(), anyString());
        then(countryJpaRepository).should(times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void save_whenACountryValueIsPassed_shouldSaveNewEntity(){
        given(countryJpaRepository.save(any(CountryEntity.class))).willReturn(ScenarioFactory.COUNTRY_ENTITY_BRAZIL);

        countryJPAPersistenceAdapter.save(Country
                .builder()
                        .countryName("BRAZIL")
                        .telephoneCodArea(55)
                .build());

        then(countryJpaRepository).should(times(1)).save(any(CountryEntity.class));
    }

    @Test
    void update_whenCountryDoesNotExists_shouldThrowNotFoundException(){
        given(countryJpaRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> countryJPAPersistenceAdapter.update(Country
                .builder()
                        .id(10L)
                .build()));

        then(countryJpaRepository).should(times(1)).findById(anyLong());
    }

    @Test
    void update_whenCountryExists_shouldUpdateEntity(){
        given(countryJpaRepository.findById(anyLong())).willReturn(ScenarioFactory.OPTIONAL_COUNTRY_ENTITY_BRAZIL);
        given(countryJpaRepository.save(any(CountryEntity.class))).willReturn(ScenarioFactory.COUNTRY_ENTITY_UNITED_STATES);

        countryJPAPersistenceAdapter.update(Country
                .builder()
                        .id(1L)
                .build());

        then(countryJpaRepository).should(times(1)).findById(anyLong());
        then(countryJpaRepository).should(times(1)).save(any(CountryEntity.class));
    }

    @Test
    void delete_whenCountryDoesNotExists_shouldThrowNotFoundException(){
        given(countryJpaRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> countryJPAPersistenceAdapter.delete(Country.builder()
                        .id(1L)
                .build()));
        then(countryJpaRepository).should(times(1)).findById(anyLong());
        then(countryJpaRepository).should(times(0)).deleteById(anyLong());
    }

    @Test
    void delete_whenCountryExists_shouldDeleteEntity(){
        given(countryJpaRepository.findById(anyLong())).willReturn(ScenarioFactory.OPTIONAL_COUNTRY_ENTITY_BRAZIL);

        countryJPAPersistenceAdapter.delete(Country.builder()
                        .id(1L)
                .build());

        then(countryJpaRepository).should(times(1)).findById(anyLong());
        then(countryJpaRepository).should(times(1)).deleteById(anyLong());
    }

}
