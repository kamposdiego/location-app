package br.com.morsesystems.location.application.port.out;

import br.com.morsesystems.location.domain.Country;

public interface CountrySendMessagePort {

    void sendMessage(Country country);

}
