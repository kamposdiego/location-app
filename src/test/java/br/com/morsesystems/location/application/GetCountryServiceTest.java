package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.GetCountryService;
import br.com.morsesystems.location.application.port.out.GetCountryPort;
import br.com.morsesystems.location.application.port.in.GetCountryUseCase;
import br.com.morsesystems.location.domain.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class GetCountryServiceTest {

    @Mock
    private GetCountryPort getCountryPort;
    @InjectMocks
    private GetCountryService getCountryLocationUseCaseImpl;

    @Test
    void givenCountries_whenGetCountryById_thenShouldReturnCountry() {
        given(getCountryPort.get(anyLong())).willReturn(Country
                .builder()
                        .id(1L)
                        .countryName("Brazil")
                        .telephoneCodArea(55)
                .build());

        getCountryLocationUseCaseImpl.getCountry(GetCountryUseCase.GetCountryCommand.builder()
                        .country(Country
                                .builder()
                                .id(1L)
                                .build())
                .build());

        then(getCountryPort).should().get(anyLong());
    }

}
