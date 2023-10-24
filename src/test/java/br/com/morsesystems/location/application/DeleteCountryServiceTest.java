package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.port.out.DeleteCountryPort;
import br.com.morsesystems.location.application.port.out.CountrySendMessagePort;
import br.com.morsesystems.location.application.port.in.DeleteCountryUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        willDoNothing().given(deleteCountryPort).delete(anyLong());

        deleteCountryLocationUseCaseImpl.deleteCountry(new DeleteCountryUseCase.DeleteCountryCommand(1L));

        then(deleteCountryPort).should().delete(anyLong());
        then(countrySendMessagePort).shouldHaveNoInteractions();
    }

}
