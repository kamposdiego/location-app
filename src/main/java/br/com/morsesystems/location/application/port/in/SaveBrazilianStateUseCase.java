package br.com.morsesystems.location.application.port.in;

import br.com.morsesystems.location.domain.BrazilianState;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface SaveBrazilianStateUseCase {

    SaveBrazilianStateCommand saveBrazilianState(SaveBrazilianStateCommand command);

    @Builder
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @Getter
    class SaveBrazilianStateCommand {
        private final BrazilianState brazilianState;
        private final String xIdempotencyKey;
    }
}
