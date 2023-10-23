package br.com.morsesystems.location.application.port.out;

import br.com.morsesystems.location.domain.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetCountryPort {

    Country get(Long countryId);

    Page<Country> getCountries(Pageable pageable, String filter);

}
