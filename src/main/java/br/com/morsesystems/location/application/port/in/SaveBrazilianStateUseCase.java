package br.com.morsesystems.location.application.port.in;

import br.com.morsesystems.location.domain.BrazilianState;
import br.com.morsesystems.location.shared.SelfValidating;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public interface SaveBrazilianStateUseCase {

    BrazilianState saveBrazilianState(SaveBrazilianStateCommand command);


    @EqualsAndHashCode(callSuper = false)
    @Getter
    class SaveBrazilianStateCommand extends SelfValidating<SaveBrazilianStateCommand> {
        @NotNull(message = "Brazilian state data is necessary.")
        private final BrazilianState brazilianState;
        @NotNull(message = "X-IdempotencyKey is necessary.")
        private final String xIdempotencyKey;

        public SaveBrazilianStateCommand(BrazilianState brazilianState, String xIdempotencyKey){
            this.brazilianState = brazilianState;
            this.xIdempotencyKey = xIdempotencyKey;
            this.validateSelf();
        }

    }
}
