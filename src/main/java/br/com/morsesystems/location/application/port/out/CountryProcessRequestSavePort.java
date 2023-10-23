package br.com.morsesystems.location.application.port.out;

import br.com.morsesystems.location.domain.CountryProcessRequest;

public interface CountryProcessRequestSavePort {

    CountryProcessRequest save(CountryProcessRequest countryProcessRequest);

}
