package br.com.morsesystems.location.application.port.in;

import br.com.morsesystems.location.domain.Country;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface GetCountryUseCase {

    GetCountryCommand getCountry(GetCountryCommand command);

    @Builder
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @Getter
    class GetCountryCommand {
        private final Country country;
    }

}
