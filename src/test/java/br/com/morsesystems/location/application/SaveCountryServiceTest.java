package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.exception.CountryRequestProcessedException;
import br.com.morsesystems.location.application.port.out.CountryProcessRequestExistsByIdPort;
import br.com.morsesystems.location.application.port.out.CountryProcessRequestSavePort;
import br.com.morsesystems.location.application.port.out.SaveCountryPort;
import br.com.morsesystems.location.application.port.out.CountrySendMessagePort;
import br.com.morsesystems.location.application.port.in.SaveCountryUseCase;
import br.com.morsesystems.location.domain.Country;
import br.com.morsesystems.location.domain.CountryProcessRequest;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class SaveCountryServiceTest {

    @Mock
    private SaveCountryPort saveCountryPort;
    @Mock
    private CountryProcessRequestExistsByIdPort countryProcessRequestExistsByIdPort;
    @Mock
    private CountryProcessRequestSavePort countryProcessRequestSavePort;
    @Mock
    private CountrySendMessagePort countrySendMessagePort;
    @InjectMocks
    private SaveCountryService saveCountryLocationUseCaseImpl;

    @Test
    void givenCountries_whenSaveCountryAndCountryIsNull_shouldConstraintViolationException() {
        assertThrows(ConstraintViolationException.class, () -> saveCountryLocationUseCaseImpl.saveCountry(new SaveCountryUseCase.SaveCountryCommand
                        (null, "63523793-215a-4bd7-acc6-21aacc12b197")));

        then(countryProcessRequestExistsByIdPort).shouldHaveNoInteractions();
        then(saveCountryPort).shouldHaveNoInteractions();
        then(countryProcessRequestSavePort).shouldHaveNoInteractions();
        then(countrySendMessagePort).shouldHaveNoInteractions();
    }

    @Test
    void givenCountries_whenSaveCountry_thenShouldSaveCountry() {
        given(countryProcessRequestExistsByIdPort.existsById(anyString())).willReturn(false);
        given(saveCountryPort.save(any(Country.class))).willReturn(Country
                .builder()
                        .id(1L)
                        .countryName("Brazil")
                        .telephoneCodArea(55)
                .build());
        given(countryProcessRequestSavePort.save(any(CountryProcessRequest.class)))
                .willReturn(CountryProcessRequest
                        .builder()
                        .xIdempotencyKey("63523793-215a-4bd7-acc6-21aacc12b197")
                        .processDateTime(LocalDateTime.of(2021, 1, 1, 0, 0, 0))
                        .build());

        saveCountryLocationUseCaseImpl.saveCountry(new SaveCountryUseCase.SaveCountryCommand
                (Country.builder().build(), "63523793-215a-4bd7-acc6-21aacc12b197"));

        then(countryProcessRequestExistsByIdPort).should().existsById(anyString());
        then(saveCountryPort).should().save(any(Country.class));
        then(countryProcessRequestSavePort).should().save(any(CountryProcessRequest.class));
        then(countrySendMessagePort).shouldHaveNoInteractions();
    }

    @Test
    void givenCountries_whenSaveCountryAndRequestAlreadyProcessed_thenShouldThrowsCountryRequestProcessedException() {
        given(countryProcessRequestExistsByIdPort.existsById(anyString())).willReturn(true);

        assertThrows(CountryRequestProcessedException.class,() ->
                saveCountryLocationUseCaseImpl.saveCountry(new SaveCountryUseCase.SaveCountryCommand
                        (Country
                                .builder()
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                                .build(), "63523793-215a-4bd7-acc6-21aacc12b197")));

        then(countryProcessRequestExistsByIdPort).should().existsById(anyString());
        then(saveCountryPort).shouldHaveNoInteractions();
        then(countryProcessRequestSavePort).shouldHaveNoInteractions();
        then(countrySendMessagePort).shouldHaveNoInteractions();
    }

}
