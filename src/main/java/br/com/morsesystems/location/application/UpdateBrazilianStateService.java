package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.port.out.UpdateBrazilianStatePort;
import br.com.morsesystems.location.application.port.in.UpdateBrazilianStateUseCase;
import br.com.morsesystems.location.domain.BrazilianState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
class UpdateBrazilianStateService implements UpdateBrazilianStateUseCase {

    private final UpdateBrazilianStatePort updateBrazilianStatePort;

    @Override
    public BrazilianState updateBrazilianState(UpdateBrazilianStateCommand command) {
        log.info("The use case UpdateBrazilianStateUseCase was started.");

        BrazilianState brazilianState = updateBrazilianStatePort.update(command.getBrazilianState());

        log.info("The use case UpdateBrazilianStateUseCase was finished.");

        return brazilianState;
    }

}
