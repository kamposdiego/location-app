package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.GetBrazilianStateService;
import br.com.morsesystems.location.application.port.out.GetBrazilianStatePort;
import br.com.morsesystems.location.application.port.in.GetBrazilianStateUseCase.BrazilianStateCommand;
import br.com.morsesystems.location.domain.BrazilianState;
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
public class GetBrazilianStateServiceTest {

    @Mock
    private GetBrazilianStatePort getBrazilianStatePort;
    @InjectMocks
    private GetBrazilianStateService getBrazilianStateService;

    @Test
    void givenBrazilianStates_whenGetBrazilianStateById_thenShouldReturnBrazilianState() {
        given(getBrazilianStatePort.get(anyLong())).willReturn(BrazilianState.builder()
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
                .build());

        getBrazilianStateService.getBrazilianState(BrazilianStateCommand.builder()
                        .brazilianState(BrazilianState
                                .builder()
                                .id(1L)
                                .build())
                .build());

        then(getBrazilianStatePort).should().get(anyLong());
    }

}
