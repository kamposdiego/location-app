package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.port.out.UpdateBrazilianStatePort;
import br.com.morsesystems.location.application.port.in.UpdateBrazilianStateUseCase;
import br.com.morsesystems.location.domain.BrazilianState;
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
public class UpdateBrazilianStateServiceTest {

    @Mock
    private UpdateBrazilianStatePort updateBrazilianStatePort;
    @InjectMocks
    private UpdateBrazilianStateService updateBrazilianStateService;

    @Test
    void givenBrazilianStates_whenUpdateBrazilianState_thenShouldUpdateBrazilianState() {
        given(updateBrazilianStatePort.update(any(BrazilianState.class)))
                .willReturn(BrazilianState.builder()
                        .id(1L)
                        .brazilianStateName("São Paulo")
                        .stateAbbreviation("SP")
                        .country(Country
                                .builder()
                                .id(1L)
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                                .build())
                        .build());

        updateBrazilianStateService.updateBrazilianState(new UpdateBrazilianStateUseCase.UpdateBrazilianStateCommand(BrazilianState
                .builder()
                .id(1L)
                .brazilianStateName("São Paulo")
                .stateAbbreviation("SP")
                .country(Country
                        .builder()
                        .id(1L)
                        .countryName("Brazil")
                        .telephoneCodArea(55)
                        .build())
                .build()));

        then(updateBrazilianStatePort).should().update(any(BrazilianState.class));
    }

}
