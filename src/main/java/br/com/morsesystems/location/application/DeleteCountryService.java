package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.port.out.DeleteCountryPort;
import br.com.morsesystems.location.application.port.out.CountrySendMessagePort;
import br.com.morsesystems.location.application.port.in.DeleteCountryUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
class DeleteCountryService implements DeleteCountryUseCase {

    private final DeleteCountryPort deleteCountryPort;
    private final CountrySendMessagePort countrySendMessagePort;

    @Override
    public void deleteCountry(DeleteCountryCommand command) {
        log.info("The use case DeleteCountryUseCase was started.");

        deleteCountryPort.delete(command.getCountry());

        log.info("The use case DeleteCountryUseCase was finished.");
    }

}
