package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.port.out.GetCountryPort;
import br.com.morsesystems.location.application.port.in.GetCountryUseCase;
import br.com.morsesystems.location.domain.Country;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
class GetCountryService implements GetCountryUseCase {

    private final GetCountryPort getCountryPort;

    @Override
    public Country getCountry(GetCountryCommand command) {

        log.info("The use case GetCountryUseCase was started.");

        Country country = getCountryPort.get(command.getId());

        log.info("The use case GetCountryUseCase was finished.");

        return country;
    }

}