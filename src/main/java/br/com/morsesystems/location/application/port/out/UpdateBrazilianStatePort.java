package br.com.morsesystems.location.application.port.out;

import br.com.morsesystems.location.domain.BrazilianState;

public interface UpdateBrazilianStatePort {

    BrazilianState update(BrazilianState brazilianState);

}
