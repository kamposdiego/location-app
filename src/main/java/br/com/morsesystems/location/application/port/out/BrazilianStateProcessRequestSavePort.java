package br.com.morsesystems.location.application.port.out;

import br.com.morsesystems.location.domain.BrazilianStateProcessRequest;

public interface BrazilianStateProcessRequestSavePort {

    BrazilianStateProcessRequest save(BrazilianStateProcessRequest brazilianStateProcessRequest);

}
