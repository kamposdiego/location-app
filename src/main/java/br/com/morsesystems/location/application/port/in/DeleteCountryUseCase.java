package br.com.morsesystems.location.application.port.in;

import br.com.morsesystems.location.domain.Country;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface DeleteCountryUseCase {

    void deleteCountry(DeleteCountryCommand command);

    @Builder
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @Getter
    class DeleteCountryCommand {
        private final Country country;
    }

}
