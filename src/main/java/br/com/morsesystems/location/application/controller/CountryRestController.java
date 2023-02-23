package br.com.morsesystems.location.application.controller;


import br.com.morsesystems.location.api.CountryLocationEvent;
import br.com.morsesystems.location.application.usecase.CountrySaveLocationUseCase;
import br.com.morsesystems.location.domain.Country;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/country")
@RequiredArgsConstructor
public class CountryRestController {

    private final CountrySaveLocationUseCase countrySaveLocation;

    @PostMapping
    public void countrySaveLocation(@RequestBody CountryLocationEvent countryLocationEvent) {
        countrySaveLocation.saveCacheLocation(CountrySaveLocationUseCase
                .CountrySaveLocationCommand
                .builder()
                .country(Country.builder()
                        .countryName(countryLocationEvent.getCountryName())
                        .telephoneCodArea(countryLocationEvent.getTelephoneCodArea())
                        .build())
                .build());
    }


}
