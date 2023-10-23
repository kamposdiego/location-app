package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.DeleteCountryService;
import br.com.morsesystems.location.application.port.out.DeleteCountryPort;
import br.com.morsesystems.location.application.port.out.CountrySendMessagePort;
import br.com.morsesystems.location.application.port.in.DeleteCountryUseCase;
import br.com.morsesystems.location.domain.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
public class DeleteCountryServiceTest {

    @Mock
    private DeleteCountryPort deleteCountryPort;
    @Mock
    private CountrySendMessagePort countrySendMessagePort;
    @InjectMocks
    private DeleteCountryService deleteCountryLocationUseCaseImpl;

    @Test
    void givenCountries_whenDeleteCountry_thenShouldDeleteCountry() {
        willDoNothing().given(deleteCountryPort).delete(any(Country.class));

        deleteCountryLocationUseCaseImpl.deleteCountry(DeleteCountryUseCase.DeleteCountryCommand
                .builder()
                        .country(Country
                                .builder()
                                .id(1L)
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                                .build())
                .build());

        then(deleteCountryPort).should().delete(any(Country.class));
        then(countrySendMessagePort).shouldHaveNoInteractions();
    }

}
