package br.com.morsesystems.location.application.port.in;

import br.com.morsesystems.location.domain.Country;
import br.com.morsesystems.location.shared.SelfValidating;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public interface UpdateCountryUseCase {

    Country updateCountry(UpdateCountryCommand command);

    @EqualsAndHashCode(callSuper = false)
    @Getter
    class UpdateCountryCommand extends SelfValidating<UpdateCountryCommand> {

        @NotNull(message = "Country data is necessary.")
        private final Country country;

        public UpdateCountryCommand(Country country){
            this.country = country;
            this.validateSelf();
        }

    }

}
