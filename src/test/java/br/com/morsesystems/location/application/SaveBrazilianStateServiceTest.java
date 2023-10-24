package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.exception.BrazilianStateRequestProcessedException;
import br.com.morsesystems.location.application.port.in.SaveBrazilianStateUseCase;
import br.com.morsesystems.location.application.port.out.BrazilianStateProcessRequestExistsByIdPort;
import br.com.morsesystems.location.application.port.out.BrazilianStateProcessRequestSavePort;
import br.com.morsesystems.location.application.port.out.SaveBrazilianStatePort;
import br.com.morsesystems.location.domain.BrazilianState;
import br.com.morsesystems.location.domain.BrazilianStateProcessRequest;
import br.com.morsesystems.location.domain.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class SaveBrazilianStateServiceTest {

    @Mock
    private SaveBrazilianStatePort saveBrazilianStatePort;
    @Mock
    private BrazilianStateProcessRequestExistsByIdPort brazilianStateProcessRequestExistsByIdPort;
    @Mock
    private BrazilianStateProcessRequestSavePort brazilianStateProcessRequestSavePort;
    @InjectMocks
    private SaveBrazilianStateService saveBrazilianStateService;

    @Test
    void givenBrazilianStates_whenSaveBrazilianState_thenShouldSaveBrazilianState() {
        given(brazilianStateProcessRequestExistsByIdPort.existsById(anyString())).willReturn(false);
        given(saveBrazilianStatePort.save(any(BrazilianState.class))).willReturn(BrazilianState
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
                .build());
        given(brazilianStateProcessRequestSavePort.save(any(BrazilianStateProcessRequest.class)))
                .willReturn(BrazilianStateProcessRequest
                        .builder()
                        .xIdempotencyKey("63523793-215a-4bd7-acc6-21aacc12b197")
                        .processDateTime(LocalDateTime.of(2021, 1, 1, 0, 0, 0))
                        .build());

        saveBrazilianStateService.saveBrazilianState(new SaveBrazilianStateUseCase.SaveBrazilianStateCommand(
                BrazilianState
                        .builder()
                        .brazilianStateName("São Paulo")
                        .stateAbbreviation("SP")
                        .build(),  "63523793-215a-4bd7-acc6-21aacc12b197"
        ));

        then(brazilianStateProcessRequestExistsByIdPort).should().existsById(anyString());
        then(saveBrazilianStatePort).should().save(any(BrazilianState.class));
        then(brazilianStateProcessRequestSavePort).should().save(any(BrazilianStateProcessRequest.class));
    }

    @Test
    void givenBrazilianStates_whenSaveBrazilianStateAndRequestAlreadyProcessed_thenShouldThrowsBrazilianStateRequestProcessedException() {
        given(brazilianStateProcessRequestExistsByIdPort.existsById(anyString())).willReturn(true);

        assertThrows(BrazilianStateRequestProcessedException.class,() ->
                saveBrazilianStateService.saveBrazilianState(new SaveBrazilianStateUseCase.SaveBrazilianStateCommand(
                        BrazilianState
                                .builder()
                                .brazilianStateName("São Paulo")
                                .stateAbbreviation("SP")
                                .build(), "63523793-215a-4bd7-acc6-21aacc12b197"
                )));

        then(brazilianStateProcessRequestExistsByIdPort).should().existsById(anyString());
        then(saveBrazilianStatePort).shouldHaveNoInteractions();
        then(brazilianStateProcessRequestSavePort).shouldHaveNoInteractions();
    }

}
