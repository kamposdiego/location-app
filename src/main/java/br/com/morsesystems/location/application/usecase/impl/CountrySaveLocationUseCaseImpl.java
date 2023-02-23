package br.com.morsesystems.location.application.usecase.impl;

import br.com.morsesystems.location.application.port.CountrySendMessagePort;
import br.com.morsesystems.location.application.usecase.CountrySaveLocationUseCase;
import br.com.morsesystems.location.application.port.CountrySavePort;

import br.com.morsesystems.location.domain.Country;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Transactional
@Slf4j
public class CountrySaveLocationUseCaseImpl implements CountrySaveLocationUseCase {

    private final CountrySavePort countrySavePort;
    private final CountrySendMessagePort countrySendMessagePort;

    @Override
    public CountrySaveLocationCommand saveCacheLocation(CountrySaveLocationCommand command) {

        log.info("The use case CountrySaveLocationUseCaseImpl was started.");

        Country country = countrySavePort.save(command.getCountry());

        countrySendMessagePort.sendMessage(country);

        log.info("The use case CountrySaveLocationUseCaseImpl was finished.");

        return CountrySaveLocationCommand
                .builder()
                .country(country)
                .build();
    }

}