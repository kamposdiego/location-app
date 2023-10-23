package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.UpdateCountryService;
import br.com.morsesystems.location.application.port.out.CountrySendMessagePort;
import br.com.morsesystems.location.application.port.out.UpdateCountryPort;
import br.com.morsesystems.location.application.port.in.UpdateCountryUseCase;
import br.com.morsesystems.location.domain.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class UpdateCountryServiceTest {

    @Mock
    private UpdateCountryPort updateCountryPort;
    @Mock
    private CountrySendMessagePort countrySendMessagePort;
    @InjectMocks
    private UpdateCountryService updateCountryLocationUseCaseImpl;

    @Test
    void givenCountries_whenUpdateCountry_thenShouldUpdateCountry() {
        given(updateCountryPort.update(any(Country.class)))
                .willReturn(Country
                        .builder()
                        .id(1L)
                        .countryName("Canada")
                        .telephoneCodArea(2)
                        .build());

        updateCountryLocationUseCaseImpl.updateCountry(UpdateCountryUseCase.UpdateCountryCommand
                .builder()
                .country(Country
                        .builder()
                        .id(1L)
                        .countryName("Brazil")
                        .telephoneCodArea(55)
                        .build())
                .build());

        then(updateCountryPort).should().update(any(Country.class));
        then(countrySendMessagePort).shouldHaveNoInteractions();

    }

}
