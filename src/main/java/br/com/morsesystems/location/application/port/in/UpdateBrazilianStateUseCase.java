package br.com.morsesystems.location.application.port.in;

import br.com.morsesystems.location.domain.BrazilianState;
import br.com.morsesystems.location.shared.SelfValidating;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public interface UpdateBrazilianStateUseCase {

    BrazilianState updateBrazilianState(UpdateBrazilianStateCommand command);

    @EqualsAndHashCode(callSuper = false)
    @Getter
    class UpdateBrazilianStateCommand extends SelfValidating<UpdateBrazilianStateCommand> {
        @NotNull(message = "Brazilian state data is necessary.")
        private final BrazilianState brazilianState;

        public UpdateBrazilianStateCommand(BrazilianState brazilianState) {
            this.brazilianState = brazilianState;
            this.validateSelf();
        }
    }

}
