package br.com.morsesystems.location.application.port.in;

import br.com.morsesystems.location.domain.BrazilianState;
import br.com.morsesystems.location.shared.SelfValidating;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;

public interface GetBrazilianStateUseCase {
    BrazilianState getBrazilianState(GetBrazilianStateCommand command);

    @EqualsAndHashCode(callSuper = false)
    @Getter
    class GetBrazilianStateCommand extends SelfValidating<GetBrazilianStateCommand> {

        @NotNull(message = "Brazilian state ID is necessary to perform delete operation.")
        @Min(value=Long.MIN_VALUE)
        @Max(value=Long.MAX_VALUE)
        private final Long id;

        public GetBrazilianStateCommand(Long id){
            this.id = id;
            this.validateSelf();
        }
    }
}
