package br.com.morsesystems.location.application.port;

import br.com.morsesystems.location.domain.Country;

public interface CountrySavePort {

    Country save(Country country);

}
