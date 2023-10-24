package br.com.morsesystems.location.adapter.out.persistence.jpa;

import br.com.morsesystems.location.application.port.out.CountryProcessRequestExistsByIdPort;
import br.com.morsesystems.location.application.port.out.CountryProcessRequestSavePort;
import br.com.morsesystems.location.domain.CountryProcessRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
class CountryProcessRequestJPAPersistenceAdapter implements CountryProcessRequestExistsByIdPort, CountryProcessRequestSavePort {

    private final CountryProcessRequestJpaRepository countryProcessRequestJpaRepository;

    @Override
    public Boolean existsById(String xIdempotencyKey) {
        log.info(String.format("CountryProcessRequest with xIdempotencyKey %s is in search.", xIdempotencyKey));

        return countryProcessRequestJpaRepository.existsById(xIdempotencyKey);
    }

    @Override
    public CountryProcessRequest save(CountryProcessRequest countryProcessRequest) {
        log.info(String.format("CountryProcessRequest with xIdempotencyKey %s is in save.", countryProcessRequest.getXIdempotencyKey()));

        CountryProcessRequestJpaEntity countryProcessRequestJpaEntity = countryProcessRequestJpaRepository.save(CountryProcessRequestJpaEntity
                .builder()
                        .xIdempotencyKey(countryProcessRequest.getXIdempotencyKey())
                        .processDateTime(LocalDateTime.now())
                .build());

        log.info(String.format("CountryProcessRequest with xIdempotencyKey %s was saved.", countryProcessRequestJpaEntity.getXIdempotencyKey()));

        return CountryProcessRequest.builder()
                .xIdempotencyKey(countryProcessRequestJpaEntity.getXIdempotencyKey())
                .processDateTime(countryProcessRequestJpaEntity.getProcessDateTime())
                .build();
    }

}
