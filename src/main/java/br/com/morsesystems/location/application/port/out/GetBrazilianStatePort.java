package br.com.morsesystems.location.application.port.out;

import br.com.morsesystems.location.domain.BrazilianState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetBrazilianStatePort {

    BrazilianState get(Long brazilianStateId);

    Page<BrazilianState> getBrazilianStates(Pageable pageable, String filter);

}
