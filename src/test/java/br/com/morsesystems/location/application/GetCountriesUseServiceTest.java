package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.port.out.GetCountryPort;
import br.com.morsesystems.location.domain.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class GetCountriesUseServiceTest {

    @Mock
    private GetCountryPort getCountryPort;
    @InjectMocks
    private GetCountriesUseService getCountriesLocationUseCaseImpl;

    @Test
    void givenCountries_whenGetCountries_thenShouldReturnCountries() {
        given(getCountryPort.getCountries(any(Pageable.class), eq(null))).willReturn(
                new PageImpl<>(Arrays.asList(Country
                        .builder()
                                .id(1L)
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                        .build()), Pageable.unpaged(), 1));

        getCountriesLocationUseCaseImpl.getCountries(Pageable.unpaged(), null);

        then(getCountryPort).should().getCountries(any(Pageable.class), eq(null));
    }

}
