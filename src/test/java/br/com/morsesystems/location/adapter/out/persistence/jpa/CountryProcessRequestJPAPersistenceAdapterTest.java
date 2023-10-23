package br.com.morsesystems.location.adapter.out.persistence.jpa;

import br.com.morsesystems.location.adapter.out.persistence.jpa.CountryProcessRequestJPAPersistenceAdapter;
import br.com.morsesystems.location.adapter.out.persistence.jpa.CountryProcessRequestJpaRepository;
import br.com.morsesystems.location.adapter.out.persistence.jpa.CountryProcessRequestEntity;
import br.com.morsesystems.location.domain.CountryProcessRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class CountryProcessRequestJPAPersistenceAdapterTest {

    @InjectMocks
    private CountryProcessRequestJPAPersistenceAdapter countryProcessRequestJPAPersistenceAdapter;

    @Mock
    private CountryProcessRequestJpaRepository countryProcessRequestJpaRepository;

    @Test
    void givenCountryProcessRequest_whenExistsById_shouldReturnTrue() {
        given(countryProcessRequestJpaRepository.existsById(anyString())).willReturn(true);

        Boolean result = countryProcessRequestJPAPersistenceAdapter.existsById("63523793-215a-4bd7-acc6-21aacc12b197");

        assertTrue(result);
        then(countryProcessRequestJpaRepository).should().existsById(anyString());
    }

    @Test
    void givenCountryProcessRequest_whenNotExistsById_shouldReturnFalse() {
        given(countryProcessRequestJpaRepository.existsById(anyString())).willReturn(false);

        Boolean result = countryProcessRequestJPAPersistenceAdapter.existsById("63523793-215a-4bd7-acc6-21aacc12b197");

        assertFalse(result);
        then(countryProcessRequestJpaRepository).should().existsById(anyString());
    }

    @Test
    void givenCountryProcessRequest_whenSave_shouldReturnCountryProcessRequest() {
        given(countryProcessRequestJpaRepository.save(any(CountryProcessRequestEntity.class))).willReturn(CountryProcessRequestEntity
                .builder().build());

        countryProcessRequestJPAPersistenceAdapter.save(CountryProcessRequest
                .builder()
                .xIdempotencyKey("63523793-215a-4bd7-acc6-21aacc12b197")
                .processDateTime(LocalDateTime.of(2021, 1, 1, 0, 0, 0))
                .build());

        then(countryProcessRequestJpaRepository).should().save(any());
    }

}
