package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.port.out.GetBrazilianStatePort;
import br.com.morsesystems.location.application.port.in.GetBrazilianStatesUseCase;
import br.com.morsesystems.location.domain.BrazilianState;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
class GetBrazilianStatesService implements GetBrazilianStatesUseCase {

    private final GetBrazilianStatePort getBrazilianStatePort;

    @Override
    public Page<BrazilianState> getBrazilianStates(Pageable pageable, String filter) {
        return getBrazilianStatePort.getBrazilianStates(pageable,filter);
    }

}
