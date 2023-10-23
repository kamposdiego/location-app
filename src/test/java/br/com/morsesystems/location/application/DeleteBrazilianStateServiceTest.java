package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.DeleteBrazilianStateService;
import br.com.morsesystems.location.application.port.out.DeleteBrazilianStatePort;
import br.com.morsesystems.location.application.port.in.DeleteBrazilianStateUseCase;
import br.com.morsesystems.location.domain.BrazilianState;
import br.com.morsesystems.location.domain.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
public class DeleteBrazilianStateServiceTest {

    @Mock
    private DeleteBrazilianStatePort deleteBrazilianStatePort;
    @InjectMocks
    private DeleteBrazilianStateService deleteBrazilianStateService;

    @Test
    void givenBrazilianStates_whenDeleteBrazilianState_thenShouldDeleteBrazilianState() {
        willDoNothing().given(deleteBrazilianStatePort).delete(any(BrazilianState.class));

        deleteBrazilianStateService.deleteBrazilianState(DeleteBrazilianStateUseCase
                .DeleteBrazilianStateCommand.builder().brazilianState(BrazilianState.builder()
                        .id(1L)
                        .brazilianStateName("São Paulo")
                        .stateAbbreviation("SP")
                        .brazilianStateIBGECod(35)
                        .country(Country
                                .builder()
                                .id(1L)
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                                .build())
                        .build()).build());

        then(deleteBrazilianStatePort).should().delete(any(BrazilianState.class));
    }

}
