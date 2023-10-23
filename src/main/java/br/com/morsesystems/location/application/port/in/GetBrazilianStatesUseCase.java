package br.com.morsesystems.location.application.port.in;

import br.com.morsesystems.location.domain.BrazilianState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetBrazilianStatesUseCase {
    Page<BrazilianState> getBrazilianStates(Pageable pageable, String filter);
}
