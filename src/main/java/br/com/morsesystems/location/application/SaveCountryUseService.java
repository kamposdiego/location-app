package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.exception.CountryRequestProcessedException;
import br.com.morsesystems.location.application.port.out.CountryProcessRequestExistsByIdPort;
import br.com.morsesystems.location.application.port.out.CountryProcessRequestSavePort;
import br.com.morsesystems.location.application.port.out.CountrySendMessagePort;
import br.com.morsesystems.location.application.port.in.SaveCountryUseCase;
import br.com.morsesystems.location.application.port.out.SaveCountryPort;

import br.com.morsesystems.location.domain.Country;
import br.com.morsesystems.location.domain.CountryProcessRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
class SaveCountryUseService implements SaveCountryUseCase {

    private final SaveCountryPort saveCountryPort;
    private final CountryProcessRequestExistsByIdPort countryProcessRequestExistsByIdPort;
    private final CountryProcessRequestSavePort countryProcessRequestSavePort;
    private final CountrySendMessagePort countrySendMessagePort;

    @Override
    public SaveCountryCommand saveCountry(SaveCountryCommand command) {

        if(Boolean.FALSE.equals(countryProcessRequestExistsByIdPort.existsById(command.getXIdempotencyKey()))){

            log.info("The use case SaveCountryUseCase was started.");

            Country country = saveCountryPort.save(command.getCountry());

            log.info("The use case SaveCountryUseCase was finished.");

            countryProcessRequestSavePort.save(CountryProcessRequest
                    .builder()
                    .xIdempotencyKey(command.getXIdempotencyKey())
                    .build());

            return SaveCountryCommand
                    .builder()
                    .country(country)
                    .build();
        }

        throw new CountryRequestProcessedException(String.format("The request with x-idempotency-key %s has already been processed.", command.getXIdempotencyKey()).toString());

    }

}