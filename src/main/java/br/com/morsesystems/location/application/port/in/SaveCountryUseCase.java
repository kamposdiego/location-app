package br.com.morsesystems.location.application.port.in;

import br.com.morsesystems.location.domain.Country;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface SaveCountryUseCase {

    SaveCountryCommand saveCountry(SaveCountryCommand command);

    @Builder
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @Getter
    class SaveCountryCommand {
        private final Country country;
        private final String xIdempotencyKey;
    }

}
