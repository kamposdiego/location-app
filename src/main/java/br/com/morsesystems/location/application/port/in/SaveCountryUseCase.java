package br.com.morsesystems.location.application.port.in;

import br.com.morsesystems.location.domain.Country;
import br.com.morsesystems.location.shared.SelfValidating;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public interface SaveCountryUseCase {

    Country saveCountry(SaveCountryCommand command);

    @EqualsAndHashCode(callSuper = false)
    @Getter
    class SaveCountryCommand extends SelfValidating<SaveCountryCommand> {

        @NotNull(message = "Country data is necessary.")
        private final Country country;

        @NotNull(message = "X-IdempotencyKey is necessary.")
        private final String xIdempotencyKey;

        public SaveCountryCommand(Country country, String xIdempotencyKey){
            this.country = country;
            this.xIdempotencyKey = xIdempotencyKey;
            this.validateSelf();
        }

    }

}
