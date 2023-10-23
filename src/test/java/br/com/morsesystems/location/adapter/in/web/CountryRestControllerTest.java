package br.com.morsesystems.location.adapter.in.web;

import br.com.morsesystems.location.application.exception.CountryRequestProcessedException;
import br.com.morsesystems.location.application.exception.InvalidFilterParameterException;
import br.com.morsesystems.location.application.exception.NotFoundException;
import br.com.morsesystems.location.application.port.in.*;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = CountryRestController.class)
public class CountryRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @SpyBean
    private CountryRepresentationModelAssembler countryRepresentationModelAssembler;
    @MockBean
    private GetCountryUseCase countryGetLocation;
    @MockBean
    private GetCountriesUseCase getCountriesUseCase;
    @MockBean
    private SaveCountryUseCase saveCountry;
    @MockBean
    private UpdateCountryUseCase updateCountryUseCase;
    @MockBean
    private DeleteCountryUseCase deleteCountryUseCase;

    @Test
    @DisplayName("Should return HTTP status 200 and a country")
    void givenCountries_whenGetCountriesShouldReturnCountryList() throws Exception {

        when(getCountriesUseCase.getCountries(Pageable.ofSize(10), null)).thenReturn(
                new PageImpl<>(Arrays.asList(Country.builder().id(1L).countryName("Brazil").telephoneCodArea(55).build()), Pageable.unpaged(), 1));

        mockMvc.perform(get("/countries")
                .param("size", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return http status 400 when RuntimeException was thrown")
    void givenCountries_whenGetCountriesShouldThrowRuntimeException() throws Exception {
        when(getCountriesUseCase.getCountries(Pageable.ofSize(10), null)).thenThrow(new RuntimeException("Some error."));

        mockMvc.perform(get("/countries")
                .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return http status 400 when InvalidFilterParameterException was thrown")
    void givenCountries_whenGetCountriesShouldThrowInvalidFilterParameterException() throws Exception {
        when(getCountriesUseCase.getCountries(Pageable.ofSize(10), null)).thenThrow(new InvalidFilterParameterException("Could not resolve attribute 'stateName' of Country"));

        mockMvc.perform(get("/countries")
                        .param("filter", "stateName:SP")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return http status 429 when RequestNotPermitted was thrown")
    void countries_whenGetCountriesShouldThrowRequestNotPermitted() throws Exception {
        when(getCountriesUseCase.getCountries(Pageable.ofSize(10), null)).thenThrow(RequestNotPermitted.createRequestNotPermitted(RateLimiter.ofDefaults("rateLimiterApi")));

        mockMvc.perform(get("/countries")
                .param("size", "10"))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    @DisplayName("Should return http status 400 and country")
    void givenCountries_whenGetCountryShouldReturnCountry() throws Exception {
        when(countryGetLocation.getCountry(any(GetCountryUseCase.GetCountryCommand.class))).thenReturn(
                GetCountryUseCase.GetCountryCommand
                        .builder()
                        .country(new Country(1L, "Brazil", 55))
                        .build());

        mockMvc.perform(get("/countries/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return http status 400 when RuntimeException was thrown")
    void givenCountries_whenGetCountryShouldThrowRuntimeException() throws Exception {
        when(countryGetLocation.getCountry(any(GetCountryUseCase.GetCountryCommand.class))).thenThrow(new RuntimeException("Some error."));

        mockMvc.perform(get("/countries/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return http status 404 when country not found")
    void givenCountries_whenGetCountryShouldThrowNotFoundException() throws Exception {
        when(countryGetLocation.getCountry(any(GetCountryUseCase.GetCountryCommand.class)))
                .thenThrow(new NotFoundException("Country not found."));

        mockMvc.perform(get("/countries/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return http status 429 when RequestNotPermitted was thrown")
    void givenCountries_whenGetCountryShouldThrowException() throws Exception {
        when(countryGetLocation.getCountry(any(GetCountryUseCase.GetCountryCommand.class))).thenThrow(RequestNotPermitted.createRequestNotPermitted(RateLimiter.ofDefaults("rateLimiterApi")));

        mockMvc.perform(get("/countries/1"))
                .andExpect(status().isTooManyRequests());
    }

    @Test
    @DisplayName("Should return http status 201 when save a country")
    void givenCountries_whenSaveShouldSaveCountry() throws Exception {
        when(saveCountry.saveCountry(any(SaveCountryUseCase.SaveCountryCommand.class))).thenReturn(
                SaveCountryUseCase.SaveCountryCommand
                        .builder()
                        .xIdempotencyKey("63523793-215a-4bd7-acc6-21aacc12b197")
                        .country(new Country(1L, "Brazil", 55))
                        .build());

        mockMvc.perform(post("/countries")
                .header("X-Idempotency-Key", "63523793-215a-4bd7-acc6-21aacc12b197")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new Country(1L, "Brazil", 55))))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return http status 400 when RuntimeException was thrown")
    void givenCountries_whenSaveShouldThrowRuntimeException() throws Exception {
        when(saveCountry.saveCountry(any(SaveCountryUseCase.SaveCountryCommand.class))).thenThrow(new RuntimeException("Some error."));

        mockMvc.perform(post("/countries")
                        .header("X-Idempotency-Key", "63523793-215a-4bd7-acc6-21aacc12b197")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new Country(1L, "Brazil", 55))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return http status 409 when CountryRequestProcessedException was thrown")
    void givenCountries_whenSaveShouldThrowCountryRequestProcessedException() throws Exception {
        when(saveCountry.saveCountry(any(SaveCountryUseCase.SaveCountryCommand.class)))
                .thenThrow(new CountryRequestProcessedException("The request with x-idempotency-key 63523793-215a-4bd7-acc6-21aacc12b197 has already been processed."));

        mockMvc.perform(post("/countries")
                        .header("X-Idempotency-Key", "63523793-215a-4bd7-acc6-21aacc12b197")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new Country(1L, "Brazil", 55))))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Should update a country")
    void givenCountries_whenUpdateCountryShouldUpdateCountry() throws Exception {
        when(updateCountryUseCase.updateCountry(any(UpdateCountryUseCase.UpdateCountryCommand.class))).thenReturn(
                UpdateCountryUseCase.UpdateCountryCommand
                        .builder()
                        .country(new Country(1L, "Brazil", 55))
                        .build());

        mockMvc.perform(put("/countries")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(new Country(1L, "Brazil", 55))))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should return http status 400 when RuntimeException was thrown")
    void givenCountries_whenUpdateCountryShouldThrowRuntimeException() throws Exception {
        when(updateCountryUseCase.updateCountry(any(UpdateCountryUseCase.UpdateCountryCommand.class)))
                .thenThrow(new RuntimeException("Some error."));

        mockMvc.perform(put("/countries")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new Country(1L, "Brazil", 55))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return http status 404 when NotFoundException was thrown")
    void givenCountries_whenUpdateCountryShouldThrowNotFoundException() throws Exception {
        when(updateCountryUseCase.updateCountry(any(UpdateCountryUseCase.UpdateCountryCommand.class)))
                .thenThrow(new NotFoundException("Country not found."));

        mockMvc.perform(put("/countries")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(new Country(1L, "Brazil", 55))))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Should return http status 204 when delete a country")
    void givenCountries_whenDeleteCountryShouldDeleteCountry() throws Exception {
        doNothing().when(deleteCountryUseCase).deleteCountry(any(DeleteCountryUseCase.DeleteCountryCommand.class));

        mockMvc.perform(delete("/countries/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return http status 400 when delete a country")
    void givenCountries_whenDeleteCountryShouldThrowRuntimeException() throws Exception {
        doThrow(new RuntimeException("Some error.")).when(deleteCountryUseCase).deleteCountry(any(DeleteCountryUseCase.DeleteCountryCommand.class));

        mockMvc.perform(delete("/countries/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return http status 404 when delete a country")
    void givenCountries_whenDeleteCountryShouldThrowNotFoundException() throws Exception {
        doThrow(new NotFoundException("Country not found.")).when(deleteCountryUseCase).deleteCountry(any(DeleteCountryUseCase.DeleteCountryCommand.class));

        mockMvc.perform(delete("/countries/1"))
                .andExpect(status().isNotFound());
    }


}