package br.com.morsesystems.location.adapter.out.persistence.jpa;

import br.com.morsesystems.location.domain.BrazilianStateProcessRequest;
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
public class BrazilianStateProcessRequestJPAPersistenceAdapterTest {

    @InjectMocks
    private BrazilianStateProcessRequestJPAPersistenceAdapter brazilianStateProcessRequestJPAPersistenceAdapter;

    @Mock
    private BrazilianStateProcessRequestJpaRepository brazilianStateProcessRequestJpaRepository;

    @Test
    void givenBrazilianStateProcessRequest_whenExistsById_shouldReturnTrue() {
        given(brazilianStateProcessRequestJpaRepository.existsById(anyString())).willReturn(true);

        Boolean result = brazilianStateProcessRequestJPAPersistenceAdapter.existsById("63523793-215a-4bd7-acc6-21aacc12b197");

        assertTrue(result);
        then(brazilianStateProcessRequestJpaRepository).should().existsById(anyString());
    }

    @Test
    void givenBrazilianStateProcessRequest_whenNotExistsById_shouldReturnFalse() {
        given(brazilianStateProcessRequestJpaRepository.existsById(anyString())).willReturn(false);

        Boolean result = brazilianStateProcessRequestJPAPersistenceAdapter.existsById("63523793-215a-4bd7-acc6-21aacc12b197");

        assertFalse(result);
        then(brazilianStateProcessRequestJpaRepository).should().existsById(anyString());
    }

    @Test
    void givenBrazilianStateProcessRequest_whenSave_shouldReturnBrazilianStateProcessRequest() {
        given(brazilianStateProcessRequestJpaRepository.save(any(BrazilianStateProcessRequestJpaEntity.class))).willReturn(BrazilianStateProcessRequestJpaEntity
                .builder().build());

        brazilianStateProcessRequestJPAPersistenceAdapter.save(BrazilianStateProcessRequest
                .builder()
                .xIdempotencyKey("63523793-215a-4bd7-acc6-21aacc12b197")
                .processDateTime(LocalDateTime.of(2021, 1, 1, 0, 0, 0))
                .build());

        then(brazilianStateProcessRequestJpaRepository).should().save(any());
    }

}
