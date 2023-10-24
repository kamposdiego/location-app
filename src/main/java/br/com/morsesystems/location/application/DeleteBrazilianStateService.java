package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.port.out.DeleteBrazilianStatePort;
import br.com.morsesystems.location.application.port.in.DeleteBrazilianStateUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
class DeleteBrazilianStateService implements DeleteBrazilianStateUseCase {

    private final DeleteBrazilianStatePort deleteBrazilianStatePort;

    @Override
    public void deleteBrazilianState(DeleteBrazilianStateCommand command) {
        log.info("The use case DeleteBrazilianStateUseCase was started.");

        deleteBrazilianStatePort.delete(command.getId());

        log.info("The use case DeleteBrazilianStateUseCase was finished.");
    }

}
