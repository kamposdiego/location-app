package br.com.morsesystems.location.application.port.in;

import br.com.morsesystems.location.domain.Country;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface UpdateCountryUseCase {

    UpdateCountryCommand updateCountry(UpdateCountryCommand command);

    @Builder
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @Getter
    class UpdateCountryCommand {
        private final Country country;
    }

}
