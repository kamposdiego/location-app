package br.com.morsesystems.location.adapter.in.web;

import br.com.morsesystems.location.application.port.in.*;
import br.com.morsesystems.location.domain.Country;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping
@RequiredArgsConstructor
@Validated
@Tag(name = "Countries", description = "Endpoints for managing countries")
class CountryRestController {

    public static final String COUNTRIES = "/countries";
    private final CountryRepresentationModelAssembler countryRepresentationModelAssembler;
    private final GetCountryUseCase countryGetLocation;
    private final GetCountriesUseCase getCountriesUseCase;
    private final SaveCountryUseCase countrySaveLocation;
    private final UpdateCountryUseCase updateCountryUseCase;
    private final DeleteCountryUseCase deleteCountryUseCase;

    @Operation(
            operationId = "getCountryById",
            summary = "Get a country by id",
            description = "Retrives the details of a country by id",
            tags = {"Countries"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Country succesfully retrived", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Country.class)
                            )}),
                    @ApiResponse(responseCode = "400", description = "Invalid request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )}),
                    @ApiResponse(responseCode = "404", description = "Country not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )}),
                    @ApiResponse(responseCode = "429", description = "The consumer has sent too many requests in a giver amount of time", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )}),
                    @ApiResponse(responseCode = "500", description = "Internal unexpected error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )})
            }
    )
    @RequestMapping(method = RequestMethod.GET,
            value = COUNTRIES+"/{country-id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @RateLimiter(name = "rateLimiterApi")
    public ResponseEntity<EntityModel<Country>> getCountryById(@Parameter(name = "country-id", description = "Country identifier.", required = true, in = ParameterIn.PATH) @PathVariable("country-id") Long id) {

        Country country = countryGetLocation.getCountry(new GetCountryUseCase.GetCountryCommand(id));

        return ResponseEntity.ok(countryRepresentationModelAssembler.toModel(country));
    }

    @Operation(
            operationId = "getCountries",
            summary = "Get all countries",
            description = "Retrives all countries",
            tags = {"Countries"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Countries found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Country.class)
                            )}),
                    @ApiResponse(responseCode = "400", description = "Invalid request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )}),
                    @ApiResponse(responseCode = "429", description = "The consumer has sent too many requests in a giver amount of time", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )}),
                    @ApiResponse(responseCode = "500", description = "Internal unexpected error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )})
            }
    )
    @RequestMapping(method = RequestMethod.GET,
            value = COUNTRIES,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @RateLimiter(name = "rateLimiterApi")
    public ResponseEntity<Page<EntityModel<Country>>> getCountries(@RequestParam(name = "includeDeleted", required = false, defaultValue = "false") boolean includeDeleted,
                                                                   @RequestParam(value = "filter", required = false) String filter, Pageable pageable) {

        Page<Country> countries = getCountriesUseCase
                .getCountries(pageable, filter);

        CollectionModel<EntityModel<Country>> a = countryRepresentationModelAssembler.toCollectionModel(countries.getContent());

        return ResponseEntity.ok(new PageImpl<>(a.getContent().stream().collect(Collectors.toList()), pageable, countries.getTotalElements()));
    }

    @Operation(
            operationId = "saveCountry",
            description = "Save a country",
            tags = {"Countries"},
            responses = {
                @ApiResponse(responseCode = "201", description = "Country saved", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Country.class)
                )}),
                @ApiResponse(responseCode = "400", description = "Invalid request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                )}),
                @ApiResponse(responseCode = "409", description = "Country already exists", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                )}),
                @ApiResponse(responseCode = "500", description = "Internal unexpected error", content = {
                     @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                )})
            }
    )
    @RequestMapping(method = RequestMethod.POST,
            value = COUNTRIES,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EntityModel<Country>> saveCountry(@Parameter(name = "x-idempotency-key", description = "Idempotency key generated by client.", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "x-idempotency-key", required = true) UUID idempotencyKey, @Valid @RequestBody(required = true) Country country) {
        Country result = countrySaveLocation.saveCountry(
                new SaveCountryUseCase.SaveCountryCommand(country, idempotencyKey.toString()));

        EntityModel<Country> entityModel = countryRepresentationModelAssembler.toModel(result);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(COUNTRIES)
                .path("/{id}")
                .buildAndExpand(country.getId()).toUri()).body(entityModel);
    }

    @Operation(
            operationId = "updateCountry",
            description = "Update a country",
            tags = {"Countries"},
            responses = {
                @ApiResponse(responseCode = "200", description = "Country updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Country.class)
                )}),
                @ApiResponse(responseCode = "400", description = "Invalid request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                )}),
                @ApiResponse(responseCode = "404", description = "Country not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                )}),
                @ApiResponse(responseCode = "500", description = "Internal unexpected error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                )})
            }
    )
    @RequestMapping(method = RequestMethod.PUT,
            value = COUNTRIES,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EntityModel<Country>> updateCountry(@RequestBody Country countryLocationEvent) {

        Country country = updateCountryUseCase.updateCountry(new UpdateCountryUseCase.UpdateCountryCommand(
                Country.builder()
                        .id(countryLocationEvent.getId())
                        .countryName(countryLocationEvent.getCountryName())
                        .telephoneCodArea(countryLocationEvent.getTelephoneCodArea())
                        .build()));

        EntityModel<Country> entityModel = countryRepresentationModelAssembler.toModel(country);

        return ResponseEntity.ok(entityModel);
    }

    @Operation(
            operationId = "deleteCountry",
            description = "Delete a country",
            tags = {"Countries"},
            responses = {
                @ApiResponse(responseCode = "204", description = "Country deleted"),
                @ApiResponse(responseCode = "400", description = "Invalid request", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                )}),
                @ApiResponse(responseCode = "404", description = "Country not found", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                )}),
                @ApiResponse(responseCode = "500", description = "Internal unexpected error", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                )})
            }
    )
    @RequestMapping(method = RequestMethod.DELETE,
            value = "/countries/{country-id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> deleteCountry(@Parameter(name = "country-id", description = "Country identifier.", required = true, in = ParameterIn.PATH) @PathVariable("country-id") Long id) {

        deleteCountryUseCase.deleteCountry(new DeleteCountryUseCase.DeleteCountryCommand(id));

        return ResponseEntity.noContent().build();
    }

}