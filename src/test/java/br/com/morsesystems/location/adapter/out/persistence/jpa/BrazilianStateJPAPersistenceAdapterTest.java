package br.com.morsesystems.location.adapter.out.persistence.jpa;

import br.com.morsesystems.location.application.exception.InvalidFilterParameterException;
import br.com.morsesystems.location.application.exception.NotFoundException;
import br.com.morsesystems.location.domain.BrazilianState;
import br.com.morsesystems.location.domain.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(SpringExtension.class)
public class BrazilianStateJPAPersistenceAdapterTest {

    @MockBean
    private BrazilianStateJpaRepository brazilianStateJpaRepository;

    @MockBean
    private SpecifcationFactory specifcationFactory;

    @SpyBean
    private ModelMapper modelMapper;

    @SpyBean
    private BrazilianStateJPAPersistenceAdapter brazilianStateJPAPersistenceAdapter;

    private Page<BrazilianStateJpaEntity> getPageBrazilianStates(){
        return new PageImpl<>(Arrays.asList(
                BrazilianStateJpaEntity.builder()
                        .id(1L)
                        .country(CountryJpaEntity.builder()
                                .id(1L)
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                                .build())
                        .brazilianStateName("São Paulo")
                        .stateAbbreviation("SP")
                        .build(),
                BrazilianStateJpaEntity.builder()
                        .id(2L)
                        .country(CountryJpaEntity.builder()
                                .id(1L)
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                                .build())
                        .brazilianStateName("Alagoas")
                        .stateAbbreviation("AL")
                        .build(),
                BrazilianStateJpaEntity.builder()
                        .id(3L)
                        .country(CountryJpaEntity.builder()
                                .id(1L)
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                                .build())
                        .brazilianStateName("Rio de Janeiro")
                        .stateAbbreviation("RJ")
                        .build()
                ), Pageable.unpaged(), 3);
    }

    @Test
    void get_whenBrazilianStateIdIsNotNull_shouldReturnCountry() {
        given(brazilianStateJpaRepository.findById(anyLong())).willReturn(Optional.of(BrazilianStateJpaEntity.builder()
                .id(1L)
                .country(CountryJpaEntity.builder()
                        .id(1L)
                        .countryName("Brazil")
                        .telephoneCodArea(55)
                        .build())
                .brazilianStateName("São Paulo")
                .stateAbbreviation("SP")
                        .brazilianStateIBGECod(35)
                .build()));

        BrazilianState brazilianState = brazilianStateJPAPersistenceAdapter.get(1L);

        then(brazilianStateJpaRepository).should(times(1)).findById(anyLong());
        assertEquals(1L, brazilianState.getId());
        assertEquals("São Paulo", brazilianState.getBrazilianStateName());
        assertEquals("SP", brazilianState.getStateAbbreviation());
    }

    @Test
    void get_whenBrazilianStateIdIsNull_shouldThrowNotFoundException() {
        given(brazilianStateJpaRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> brazilianStateJPAPersistenceAdapter.get(1L));
        then(brazilianStateJpaRepository).should(times(1)).findById(anyLong());
    }

    @Test
    void getBrazilianStates_whenFilterIsNull_shouldReturnCountries() {
        given(specifcationFactory.createSpecification(Country.class, null)).willReturn(null);
        given(brazilianStateJpaRepository.findAll((Specification<BrazilianStateJpaEntity>) eq(null), any(Pageable.class)))
                .willReturn(getPageBrazilianStates());

        Page<BrazilianState> brazilianStates = brazilianStateJPAPersistenceAdapter.getBrazilianStates(Pageable.unpaged(), null);

        assertEquals(3, brazilianStates.getTotalElements());
        assertEquals(1L, brazilianStates.getContent().get(0).getId());
        assertEquals("São Paulo", brazilianStates.getContent().get(0).getBrazilianStateName());
        assertEquals("SP", brazilianStates.getContent().get(0).getStateAbbreviation());

        then(brazilianStateJpaRepository).should(times(1)).findAll((Specification<BrazilianStateJpaEntity>) eq(null), any(Pageable.class));
    }

    @Test
    void getBrazilianStates_whenFilterIsCountryName_shouldReturnOneCountry(){
        given(specifcationFactory.createSpecification(any(), anyString())).willReturn(Specification.anyOf());
        given(brazilianStateJpaRepository.findAll(any(Specification.class), any(Pageable.class)))
                .willReturn(new PageImpl<>(Arrays.asList(BrazilianStateJpaEntity.builder()
                                .id(1L)
                                .country(CountryJpaEntity.builder()
                                        .id(1L)
                                        .countryName("Brazil")
                                        .telephoneCodArea(55)
                                        .build())
                                .brazilianStateName("São Paulo")
                                .stateAbbreviation("SP")
                                .brazilianStateIBGECod(35)
                                .build()
                        ), Pageable.unpaged(), 1));

        Page<BrazilianState> brazilianStates = brazilianStateJPAPersistenceAdapter.getBrazilianStates(Pageable.unpaged(), "brazilianStateName:São Paulo");

        assertEquals(1, brazilianStates.getTotalElements());
        assertEquals(1L, brazilianStates.getContent().get(0).getId());
        assertEquals("São Paulo", brazilianStates.getContent().get(0).getBrazilianStateName());
        assertEquals("SP", brazilianStates.getContent().get(0).getStateAbbreviation());

        then(specifcationFactory).should(times(1)).createSpecification(any(), anyString());
        then(brazilianStateJpaRepository).should(times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void getBrazilianStates_whenFilterIsInvalid_shouldThrowInvalidDataAccessApiUsageException(){
        given(specifcationFactory.createSpecification(any(), anyString())).willReturn(Specification.anyOf());
        given(brazilianStateJpaRepository.findAll(any(Specification.class), any(Pageable.class)))
                .willThrow(new InvalidDataAccessApiUsageException("SemanticException: org.hibernate.query.SemanticException: Could not resolve attribute 'field' of 'br.com.morsesystems.location.adapter.out.persistence.jpa.BrazilianStateJpaEntity'"));

        assertThrows(InvalidFilterParameterException.class, () -> brazilianStateJPAPersistenceAdapter.getBrazilianStates(Pageable.unpaged(), "field:razil"));


        then(specifcationFactory).should(times(1)).createSpecification(any(), anyString());
        then(brazilianStateJpaRepository).should(times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void getBrazilianStates_whenFilterIsInvalid_shouldThrowInvalidDataAccessApiUsageExceptionWithAnotherMessage(){
        given(specifcationFactory.createSpecification(any(), anyString())).willReturn(Specification.anyOf());
        given(brazilianStateJpaRepository.findAll(any(Specification.class), any(Pageable.class)))
                .willThrow(new InvalidDataAccessApiUsageException("RuntimeException:  another message'"));

        assertThrows(InvalidFilterParameterException.class, () -> brazilianStateJPAPersistenceAdapter.getBrazilianStates(Pageable.unpaged(), "field:razil"));


        then(specifcationFactory).should(times(1)).createSpecification(any(), anyString());
        then(brazilianStateJpaRepository).should(times(1)).findAll(any(Specification.class), any(Pageable.class));
    }

    @Test
    void save_whenABrazilianStateValueIsPassed_shouldSaveNewEntity(){
        given(brazilianStateJpaRepository.save(any(BrazilianStateJpaEntity.class))).willReturn(BrazilianStateJpaEntity.builder()
                .id(1L)
                .country(CountryJpaEntity.builder()
                        .id(1L)
                        .countryName("Brazil")
                        .telephoneCodArea(55)
                        .build())
                .brazilianStateName("São Paulo")
                .stateAbbreviation("SP")
                .build());

        brazilianStateJPAPersistenceAdapter.save(BrazilianState.builder()
                .id(1L)
                .country(Country.builder()
                        .id(1L)
                        .countryName("Brazil")
                        .telephoneCodArea(55)
                        .build())
                .brazilianStateName("São Paulo")
                .stateAbbreviation("SP")
                .build());

        then(brazilianStateJpaRepository).should(times(1)).save(any(BrazilianStateJpaEntity.class));
    }

    @Test
    void update_whenBrazilianStateDoesNotExists_shouldThrowNotFoundException(){
        given(brazilianStateJpaRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> brazilianStateJPAPersistenceAdapter.update(BrazilianState.builder()
                .id(1L)
                .country(Country.builder()
                        .id(1L)
                        .countryName("Brazil")
                        .telephoneCodArea(55)
                        .build())
                .brazilianStateName("São Paulo")
                .stateAbbreviation("SP")
                .build()));

        then(brazilianStateJpaRepository).should(times(1)).findById(anyLong());
    }

    @Test
    void update_whenBrazilianStateExists_shouldUpdateEntity(){
        given(brazilianStateJpaRepository.findById(anyLong())).willReturn(Optional.of(
                BrazilianStateJpaEntity.builder()
                        .id(1L)
                        .country(CountryJpaEntity.builder()
                                .id(1L)
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                                .build())
                        .brazilianStateName("São Paulo")
                        .stateAbbreviation("SP")
                        .build()
        ));
        given(brazilianStateJpaRepository.save(any(BrazilianStateJpaEntity.class))).willReturn(BrazilianStateJpaEntity.builder()
                .id(1L)
                .country(CountryJpaEntity.builder()
                        .id(1L)
                        .countryName("Brazil")
                        .telephoneCodArea(55)
                        .build())
                .brazilianStateName("São Paulo")
                .stateAbbreviation("SP")
                .build());

        brazilianStateJPAPersistenceAdapter.update(BrazilianState.builder()
                .id(1L)
                .country(Country.builder()
                        .id(1L)
                        .countryName("Brazil")
                        .telephoneCodArea(55)
                        .build())
                .brazilianStateName("São Paulo")
                .stateAbbreviation("SP")
                .build());

        then(brazilianStateJpaRepository).should(times(1)).findById(anyLong());
        then(brazilianStateJpaRepository).should(times(1)).save(any(BrazilianStateJpaEntity.class));
    }

    @Test
    void delete_whenBrazilianStateDoesNotExists_shouldThrowNotFoundException(){
        given(brazilianStateJpaRepository.findById(anyLong())).willReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> brazilianStateJPAPersistenceAdapter.delete(1L));
        then(brazilianStateJpaRepository).should(times(1)).findById(anyLong());
        then(brazilianStateJpaRepository).should(times(0)).deleteById(anyLong());
    }

    @Test
    void delete_whenBrazilianStateExists_shouldDeleteEntity(){
        given(brazilianStateJpaRepository.findById(anyLong())).willReturn(Optional.of(
                BrazilianStateJpaEntity.builder()
                        .id(1L)
                        .country(CountryJpaEntity.builder()
                                .id(1L)
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                                .build())
                        .brazilianStateName("São Paulo")
                        .stateAbbreviation("SP")
                        .build()
        ));

        brazilianStateJPAPersistenceAdapter.delete(1L);

        then(brazilianStateJpaRepository).should(times(1)).findById(anyLong());
        then(brazilianStateJpaRepository).should(times(1)).deleteById(anyLong());
    }

}