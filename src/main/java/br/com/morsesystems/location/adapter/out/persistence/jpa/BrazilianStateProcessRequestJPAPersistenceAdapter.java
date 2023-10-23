package br.com.morsesystems.location.adapter.out.persistence.jpa;

import br.com.morsesystems.location.application.port.out.BrazilianStateProcessRequestExistsByIdPort;
import br.com.morsesystems.location.application.port.out.BrazilianStateProcessRequestSavePort;
import br.com.morsesystems.location.domain.BrazilianStateProcessRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
class BrazilianStateProcessRequestJPAPersistenceAdapter implements BrazilianStateProcessRequestExistsByIdPort, BrazilianStateProcessRequestSavePort {

    private final BrazilianStateProcessRequestJpaRepository brazilianStateProcessRequestJpaRepository;

    @Override
    public Boolean existsById(String xIdempotencyKey) {
        log.info(String.format("BrazilianStateProcessRequest with xIdempotencyKey %s is in search.", xIdempotencyKey));

        return brazilianStateProcessRequestJpaRepository.existsById(xIdempotencyKey);
    }

    @Override
    public BrazilianStateProcessRequest save(BrazilianStateProcessRequest brazilianStateProcessRequest) {
        log.info(String.format("BrazilianStateProcessRequest with xIdempotencyKey %s is in save.", brazilianStateProcessRequest.getXIdempotencyKey()));

        BrazilianStateProcessRequestEntity brazilianStateProcessRequestEntity = brazilianStateProcessRequestJpaRepository.save(BrazilianStateProcessRequestEntity
                .builder()
                        .xIdempotencyKey(brazilianStateProcessRequest.getXIdempotencyKey())
                        .processDateTime(LocalDateTime.now())
                .build());

        log.info(String.format("BrazilianStateProcessRequest with xIdempotencyKey %s was saved.", brazilianStateProcessRequestEntity.getXIdempotencyKey()));

        return BrazilianStateProcessRequest.builder()
                .xIdempotencyKey(brazilianStateProcessRequestEntity.getXIdempotencyKey())
                .processDateTime(brazilianStateProcessRequestEntity.getProcessDateTime())
                .build();
    }

}
