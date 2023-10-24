package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.exception.BrazilianStateRequestProcessedException;
import br.com.morsesystems.location.application.port.in.SaveBrazilianStateUseCase;
import br.com.morsesystems.location.application.port.out.BrazilianStateProcessRequestExistsByIdPort;
import br.com.morsesystems.location.application.port.out.BrazilianStateProcessRequestSavePort;
import br.com.morsesystems.location.application.port.out.SaveBrazilianStatePort;
import br.com.morsesystems.location.domain.BrazilianState;
import br.com.morsesystems.location.domain.BrazilianStateProcessRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
class SaveBrazilianStateService implements SaveBrazilianStateUseCase {

    private final SaveBrazilianStatePort saveBrazilianStatePort;
    private final BrazilianStateProcessRequestExistsByIdPort brazilianStateProcessRequestExistsByIdPort;
    private final BrazilianStateProcessRequestSavePort brazilianStateProcessRequestSavePort;

    @Override
    public BrazilianState saveBrazilianState(SaveBrazilianStateCommand command) {

        if(Boolean.FALSE.equals(brazilianStateProcessRequestExistsByIdPort.existsById(command.getXIdempotencyKey()))){

            log.info("The use case SaveBrazilianStateUseCase was started.");

            BrazilianState brazilianState = saveBrazilianStatePort.save(command.getBrazilianState());

            log.info("The use case SaveBrazilianStateUseCase was finished.");

            brazilianStateProcessRequestSavePort.save(BrazilianStateProcessRequest
                    .builder()
                    .xIdempotencyKey(command.getXIdempotencyKey())
                    .build());

            return brazilianState;
        }

        throw new BrazilianStateRequestProcessedException(String.format("The request with x-idempotency-key %s has already been processed.", command.getXIdempotencyKey()).toString());

    }

}