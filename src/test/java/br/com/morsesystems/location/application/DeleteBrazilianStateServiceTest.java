package br.com.morsesystems.location.application;

import br.com.morsesystems.location.application.port.out.DeleteBrazilianStatePort;
import br.com.morsesystems.location.application.port.in.DeleteBrazilianStateUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
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
        willDoNothing().given(deleteBrazilianStatePort).delete(anyLong());

        deleteBrazilianStateService.deleteBrazilianState(new DeleteBrazilianStateUseCase
                .DeleteBrazilianStateCommand(1L));

        then(deleteBrazilianStatePort).should().delete(anyLong());
    }

}
