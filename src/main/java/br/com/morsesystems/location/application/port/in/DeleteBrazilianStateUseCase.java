package br.com.morsesystems.location.application.port.in;

import br.com.morsesystems.location.domain.BrazilianState;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface DeleteBrazilianStateUseCase {

    void deleteBrazilianState(DeleteBrazilianStateUseCase.DeleteBrazilianStateCommand command);

    @Builder
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @Getter
    class DeleteBrazilianStateCommand {
        private final BrazilianState brazilianState;
    }

}
