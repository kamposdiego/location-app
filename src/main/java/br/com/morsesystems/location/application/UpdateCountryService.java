package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.port.out.CountrySendMessagePort;
import br.com.morsesystems.location.application.port.out.UpdateCountryPort;
import br.com.morsesystems.location.application.port.in.UpdateCountryUseCase;
import br.com.morsesystems.location.domain.Country;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
class UpdateCountryService implements UpdateCountryUseCase {

    private final UpdateCountryPort updateCountryPort;
    private final CountrySendMessagePort countrySendMessagePort;

    @Override
    public Country updateCountry(UpdateCountryCommand command) {
        log.info("The use case UpdateCountryUseCase was started.");

        Country country = updateCountryPort.update(command.getCountry());

        log.info("The use case UpdateCountryUseCase was finished.");

        return country;
    }

}
