package br.com.morsesystems.location.application.port.in;

import br.com.morsesystems.location.domain.Country;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetCountriesUseCase {

    Page<Country> getCountries(Pageable pageable, String filter);

}
