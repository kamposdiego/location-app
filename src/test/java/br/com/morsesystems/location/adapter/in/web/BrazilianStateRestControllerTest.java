package br.com.morsesystems.location.adapter.in.web;

import br.com.morsesystems.location.application.exception.InvalidFilterParameterException;
import br.com.morsesystems.location.application.exception.NotFoundException;
import br.com.morsesystems.location.application.exception.BrazilianStateRequestProcessedException;
import br.com.morsesystems.location.application.port.in.*;
import br.com.morsesystems.location.domain.BrazilianState;
import br.com.morsesystems.location.domain.Country;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BrazilianStateRestController.class)
public class BrazilianStateRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @SpyBean
    private BrazilianStateRepresentationModelAssembler brazilianStateRepresentationModelAssembler;
    @MockBean
    private GetBrazilianStatesUseCase getBrazilianStatesUseCase;
    @MockBean
    private GetBrazilianStateUseCase getBrazilianStateUseCase;
    @MockBean
    private SaveBrazilianStateUseCase saveBrazilianStateUseCase;
    @MockBean
    private UpdateBrazilianStateUseCase updateBrazilianStateUseCase;
    @MockBean
    private DeleteBrazilianStateUseCase deleteBrazilianStateUseCase;

    @Test
    @DisplayName("Should return HTTP status 200 and a list of Brazilian states")
    void givenBrazilianStates_whenGetBrazilianStates_thenReturnHttpStatus200AndBrazilianStates() throws Exception {
        given(getBrazilianStatesUseCase.getBrazilianStates(any(Pageable.class), eq(null))).willReturn(
                new PageImpl<>(Arrays.asList(
                        BrazilianState.builder().brazilianStateName("São Paulo").brazilianStateIBGECod(35).stateAbbreviation("SP")
                                .country(Country.builder().id(1L).build())
                                .build()
                ), Pageable.unpaged(), 1L)
        );

        MvcResult result = mockMvc.perform(get("/brazilianstates")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("Should return HTTP status 400 when RuntimeException is thrown")
    void givenBrazilianStates_whenGetBrazilianStatesThrowRuntimeException_thenReturnHttpStatus400() throws Exception {
        given(getBrazilianStatesUseCase.getBrazilianStates(any(Pageable.class), eq(null))).willThrow(RuntimeException.class);

        mockMvc.perform(get("/brazilianstates")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return status 400 when InvalidFilterParameterException is thrown")
    void givenBrazilianStates_whenGetBrazilianStatesThrowInvalidFilterParameterException_thenReturnHttpStatus400() throws Exception {
        given(getBrazilianStatesUseCase.getBrazilianStates(any(Pageable.class), eq(null))).willThrow(new InvalidFilterParameterException(("Could not resolve attribute 'countryName' of BrazilianState")));

        mockMvc.perform(get("/brazilianstates")
                        .param("filter", "countryName:CANADA")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return status 429 when RequestNotPermitted exception is thrown")
    void givenBrazilianStates_whenGetBrazilianStatesThrowRequestNotPermittedException_thenReturnStatus429() throws Exception{
        given(getBrazilianStatesUseCase.getBrazilianStates(any(Pageable.class), eq(null))).willThrow(RequestNotPermitted.createRequestNotPermitted(RateLimiter.ofDefaults("rateLimiterApi")));

        mockMvc.perform(get("/brazilianstates")
                        .param("size", "10"))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    @DisplayName("Should return HTTP status 200 and a Brazilian state")
    void givenBrazilianStates_whenGetBrazilianState_thenReturnHttpStatus200AndBrazilianState() throws Exception {
        given(getBrazilianStateUseCase.getBrazilianState(any(GetBrazilianStateUseCase.GetBrazilianStateCommand.class))).willReturn(BrazilianState.builder().id(1L).brazilianStateName("São Paulo").brazilianStateIBGECod(35).stateAbbreviation("SP")
                .country(Country.builder().id(1L)
                        .countryName("Brazil")
                        .telephoneCodArea(55)
                        .build())
                .build());

        mockMvc.perform(get("/brazilianstates/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return HTTP status 400 when RuntimeException was thrown")
    void givenBrazilianState_whenGetBrazilianStateThrowRuntimeException_thenReturnHttpStatus400() throws Exception {
        given(getBrazilianStateUseCase.getBrazilianState(any(GetBrazilianStateUseCase.GetBrazilianStateCommand.class))).willThrow(RuntimeException.class);

        mockMvc.perform(get("/brazilianstates/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return HTTP status 404 when Brazilian state was not found")
    void givenBrazilianState_whenGetBrazilianStateThrowBrazilianStateNotFoundException_thenReturnHttpStatus404() throws Exception {
        given(getBrazilianStateUseCase.getBrazilianState(any(GetBrazilianStateUseCase.GetBrazilianStateCommand.class))).willThrow(new NotFoundException("Brazilian state not found"));

        mockMvc.perform(get("/brazilianstates/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return HTTP status 429 when RequestNotPermitted exception is thrown")
    void givenBrazilianState_whenGetBrazilianStateThrowRequestNotPermittedException_thenReturnStatus429() throws Exception{
        given(getBrazilianStateUseCase.getBrazilianState(any(GetBrazilianStateUseCase.GetBrazilianStateCommand.class))).willThrow(RequestNotPermitted.createRequestNotPermitted(RateLimiter.ofDefaults("rateLimiterApi")));

        mockMvc.perform(get("/brazilianstates/1"))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    @DisplayName("Should return HTTP status 201 when Brazilian state was created")
    void givenBrazilianState_whenSaveBrazilianStateCall_thenReturnStatus200AndBrazilianStateCreated() throws Exception {
        given(saveBrazilianStateUseCase.saveBrazilianState(any(SaveBrazilianStateUseCase.SaveBrazilianStateCommand.class))).willReturn(BrazilianState.builder().id(1L).brazilianStateName("São Paulo").brazilianStateIBGECod(35).stateAbbreviation("SP")
                .country(Country.builder().id(1L)
                        .countryName("Brazil")
                        .telephoneCodArea(55)
                        .build())
                .build());

        mockMvc.perform(post("/brazilianstates")
                .header("X-Idempotency-Key", "63523793-215a-4bd7-acc6-21aacc12b197")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(BrazilianState.builder().brazilianStateName("São Paulo").brazilianStateIBGECod(35).stateAbbreviation("SP")
                        .country(Country.builder().id(1L)
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                                .build())
                        .build()))).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return HTTP status 400 when RuntimeException was thrown")
    void givenBrazilianStates_whenSaveBrazilianStateThrowRuntimeException_thenReturnHttpStatus400() throws Exception {
        given(saveBrazilianStateUseCase.saveBrazilianState(any(SaveBrazilianStateUseCase.SaveBrazilianStateCommand.class))).willThrow(RuntimeException.class);

        mockMvc.perform(post("/brazilianstates")
                .header("X-Idempotency-Key", "63523793-215a-4bd7-acc6-21aacc12b197")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(BrazilianState.builder().brazilianStateName("São Paulo").brazilianStateIBGECod(35).stateAbbreviation("SP")
                        .country(Country.builder().id(1L)
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                                .build())
                        .build()))).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return HTTP status 409 when Brazilian state already exists")
    void givenBrazilianStates_whenBrazilianStateAlreadyExists_thenReturnHttpStatus409() throws Exception {
        given(saveBrazilianStateUseCase.saveBrazilianState(any(SaveBrazilianStateUseCase.SaveBrazilianStateCommand.class))).willThrow(
                new BrazilianStateRequestProcessedException("The request with x-idempotency-key 63523793-215a-4bd7-acc6-21aacc12b197 has already been processed."));

        mockMvc.perform(post("/brazilianstates")
                .header("X-Idempotency-Key", "63523793-215a-4bd7-acc6-21aacc12b197")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(BrazilianState.builder().brazilianStateName("São Paulo").brazilianStateIBGECod(35).stateAbbreviation("SP")
                        .country(Country.builder().id(1L)
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                                .build())
                        .build()))).andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should return HTTP status 200 when Brazilian state was updated")
    void givenBrazilianStates_whenUpdateBrazilianState_thenReturnHttpStatus200() throws Exception {
        given(updateBrazilianStateUseCase.updateBrazilianState(any(UpdateBrazilianStateUseCase.UpdateBrazilianStateCommand.class))).willReturn(
                BrazilianState.builder().id(1L).brazilianStateName("Rio de Janeiro").brazilianStateIBGECod(36).stateAbbreviation("RJ")
                        .country(Country.builder().id(1L)
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                                .build())
                        .build());

        mockMvc.perform(put("/brazilianstates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(BrazilianState.builder()
                                .brazilianStateName("São Paulo")
                                .stateAbbreviation("SP")
                                .brazilianStateIBGECod(35)
                                .id(1L)
                                .country(Country.builder().id(1L)
                                        .countryName("Brazil")
                                        .telephoneCodArea(55)
                                        .build())
                        .build()))
                ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return http status 400 when RuntimeException was thrown")
    void givenBrazilianStates_whenUpdateBrazilianState_thenReturnHttpStatus400() throws Exception {
        when(updateBrazilianStateUseCase.updateBrazilianState(any(UpdateBrazilianStateUseCase.UpdateBrazilianStateCommand.class)))
                .thenThrow(new RuntimeException("Some error."));

        mockMvc.perform(put("/brazilianstates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(BrazilianState.builder()
                        .brazilianStateName("São Paulo")
                        .stateAbbreviation("SP")
                        .brazilianStateIBGECod(35)
                        .id(1L)
                        .country(Country.builder().id(1L)
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                                .build())
                        .build()))
        ).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return http status 404 when NotFoundException was thrown")
    void givenBrazilianStates_whenUpdateBrazilianState_thenReturnHttpStatus404() throws Exception {
        when(updateBrazilianStateUseCase.updateBrazilianState(any(UpdateBrazilianStateUseCase.UpdateBrazilianStateCommand.class)))
                .thenThrow(new NotFoundException("Brazilian State not found."));

        mockMvc.perform(put("/brazilianstates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(BrazilianState.builder()
                        .brazilianStateName("São Paulo")
                        .stateAbbreviation("SP")
                        .brazilianStateIBGECod(35)
                        .id(1L)
                        .country(Country.builder().id(1L)
                                .countryName("Brazil")
                                .telephoneCodArea(55)
                                .build())
                        .build()))
        ).andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return http status 204 when delete a brazilian state")
    void givenBrazilianStates_whenDeleteBrazilianState_thenReturnHttpStatus204() throws Exception {
        doNothing().when(deleteBrazilianStateUseCase).deleteBrazilianState(any(DeleteBrazilianStateUseCase.DeleteBrazilianStateCommand.class));

        mockMvc.perform(delete("/brazilianstates/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return http status 400 when delete a brazilian state")
    void givenBrazilianStates_whenDeleteBrazilianState_thenReturnHttpStatus400() throws Exception {
        doThrow(new RuntimeException("Some error.")).when(deleteBrazilianStateUseCase).deleteBrazilianState(any(DeleteBrazilianStateUseCase.DeleteBrazilianStateCommand.class));

        mockMvc.perform(delete("/brazilianstates/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return http status 404 when delete a brazilian state")
    void givenBrazilianStates_whenDeleteBrazilianState_thenReturnHttpStatus404() throws Exception {
        doThrow(new NotFoundException("Country not found.")).when(deleteBrazilianStateUseCase).deleteBrazilianState(any(DeleteBrazilianStateUseCase.DeleteBrazilianStateCommand.class));

        mockMvc.perform(delete("/brazilianstates/1"))
                .andExpect(status().isNotFound());
    }

}
