package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.port.out.GetBrazilianStatePort;
import br.com.morsesystems.location.application.port.in.GetBrazilianStateUseCase;
import br.com.morsesystems.location.domain.BrazilianState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
class GetBrazilianStateService implements GetBrazilianStateUseCase {

    private final GetBrazilianStatePort getBrazilianStatePort;

    @Override
    public BrazilianState getBrazilianState(GetBrazilianStateCommand getBrazilianStateCommand) {

        log.info("The use case GetBrazilianStateUseCase was started.");

        BrazilianState brazilianState = getBrazilianStatePort.get(getBrazilianStateCommand.getId());

        log.info("The use case GetBrazilianStateUseCase was finished.");

        return brazilianState;
    }

}