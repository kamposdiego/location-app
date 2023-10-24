package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.port.out.GetBrazilianStatePort;
import br.com.morsesystems.location.domain.BrazilianState;
import br.com.morsesystems.location.domain.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class GetBrazilianStatesServiceTest {

    @Mock
    private GetBrazilianStatePort getBrazilianStatePort;
    @InjectMocks
    private GetBrazilianStatesService getBrazilianStatesService;

    @Test
    void givenBrazilianStates_whenGetBrazilianStates_thenShouldReturnBrazilianStates() {
        given(getBrazilianStatePort.getBrazilianStates(any(Pageable.class), eq(null))).willReturn(
                new PageImpl<>(Arrays.asList(BrazilianState.builder()
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
                        .build()), Pageable.unpaged(), 1));

        getBrazilianStatesService.getBrazilianStates(Pageable.unpaged(), null);

        then(getBrazilianStatePort).should().getBrazilianStates(any(Pageable.class), eq(null));
    }

}
