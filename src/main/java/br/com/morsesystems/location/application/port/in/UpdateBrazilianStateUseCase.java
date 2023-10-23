package br.com.morsesystems.location.application.port.in;

import br.com.morsesystems.location.domain.BrazilianState;
import br.com.morsesystems.location.domain.Country;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface UpdateBrazilianStateUseCase {

    UpdateBrazilianStateCommand updateBrazilianState(UpdateBrazilianStateCommand command);

    @Builder
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @Getter
    class UpdateBrazilianStateCommand {
        private final BrazilianState brazilianState;
    }

}
