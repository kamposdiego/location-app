package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.port.out.GetCountryPort;
import br.com.morsesystems.location.application.port.in.GetCountriesUseCase;
import br.com.morsesystems.location.domain.Country;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
class GetCountriesUseService implements GetCountriesUseCase {

    private final GetCountryPort getCountryPort;

    @Override
    public Page<Country> getCountries(Pageable pageable, String filter) {
        return getCountryPort.getCountries(pageable,filter);
    }

}
