package br.com.morsesystems.location.adapter.in.web;

import br.com.morsesystems.location.application.port.in.*;
import br.com.morsesystems.location.domain.BrazilianState;
import br.com.morsesystems.location.domain.Country;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Brazilian States", description = "Endpoints for managing brazilian states")
class BrazilianStateRestController {

    public static final String BRAZILIAN_STATES = "/brazilianstates";
    private final BrazilianStateRepresentationModelAssembler brazilianStateRepresentationModelAssembler;
    private final GetBrazilianStatesUseCase getBrazilianStatesUseCase;
    private final GetBrazilianStateUseCase getBrazilianStateUseCase;
    private final SaveBrazilianStateUseCase saveBrazilianStateUseCase;
    private final UpdateBrazilianStateUseCase updateBrazilianStateUseCase;
    private final DeleteBrazilianStateUseCase deleteBrazilianStateUseCase;

    @Operation(
            operationId = "getBrazilianStateById",
            summary = "Get a brazilian state by id",
            description = "Retrives the details of a brazilian state by id",
            tags = {"Brazilian States"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Brazilian state succesfully retrived", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = BrazilianState.class)
                            )}),
                    @ApiResponse(responseCode = "400", description = "Invalid request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )}),
                    @ApiResponse(responseCode = "404", description = "Brazilian state not found", content = {
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
            value = BRAZILIAN_STATES+"/{brazilian-state-id}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<BrazilianState>> getBrazilianStateById(@Parameter(name = "brazilian-state-id", description = "Brazilian state identifier.", required = true, in = ParameterIn.PATH) @PathVariable("brazilian-state-id") Long id) {
        BrazilianState brazilianState = getBrazilianStateUseCase
                .getBrazilianState(GetBrazilianStateUseCase.BrazilianStateCommand.builder().brazilianState(BrazilianState.builder().id(id).build()).build())
                .getBrazilianState();

        return ResponseEntity.ok(brazilianStateRepresentationModelAssembler.toModel(brazilianState));
    }

    @Operation(
            operationId = "getBrazilianStates",
            summary = "Get a list of brazilian states",
            description = "Retrieves a list of brazilian states",
            tags = {"Brazilian States"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Brazilian States found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = BrazilianState.class)
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
            })
    @RequestMapping(method = RequestMethod.GET,
            value = BRAZILIAN_STATES,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @RateLimiter(name = "rateLimiterApi")
    public ResponseEntity<Page<EntityModel<BrazilianState>>> getBrazilianStates(@RequestParam(name = "includeDeleted", required = false,defaultValue = "false") boolean includeDeleted,
                                                                   @RequestParam(name = "filter", required = false) String filter, Pageable pageable) {
        Page<BrazilianState> pageBrazilianStates = getBrazilianStatesUseCase.getBrazilianStates(pageable, filter);


        CollectionModel<EntityModel<BrazilianState>> brazilianStates =
                brazilianStateRepresentationModelAssembler.toCollectionModel(pageBrazilianStates.getContent());

        return ResponseEntity.ok(new PageImpl<>(
                brazilianStates.getContent().stream().collect(Collectors.toList()),
                pageable,
                pageBrazilianStates.getTotalElements()
        ));
    }
    @Operation(
            operationId = "saveBrazilianState",
            description = "Save a brazilian state",
            tags = {"Brazilian States"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Brazilian state saved", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = BrazilianState.class)
                            )}),
                    @ApiResponse(responseCode = "400", description = "Invalid request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )}),
                    @ApiResponse(responseCode = "409", description = "Brazilian state already exists", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )}),
                    @ApiResponse(responseCode = "500", description = "Internal unexpected error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )})
            }
    )
    @RequestMapping(method = RequestMethod.POST,
            value = BRAZILIAN_STATES,
            produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<EntityModel<BrazilianState>> saveBrazilianState(@Parameter(name = "x-idempotency-key", description = "Idempotency key generated by client.", required = true, in = ParameterIn.HEADER) @RequestHeader(value = "x-idempotency-key", required = true) UUID idempotencyKey, @Valid @RequestBody(required = true) BrazilianState brazilianState) {
        BrazilianState state = saveBrazilianStateUseCase.saveBrazilianState(SaveBrazilianStateUseCase
                .SaveBrazilianStateCommand
                .builder()
                .xIdempotencyKey(idempotencyKey.toString())
                .brazilianState(brazilianState).build()).getBrazilianState();

        EntityModel<BrazilianState> entityModel = brazilianStateRepresentationModelAssembler.toModel(state);

        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(BRAZILIAN_STATES)
                .path("/{id}")
                .buildAndExpand(state.getId()).toUri()).body(entityModel);
    }

    @Operation(
            operationId = "updateBrazilianState",
            description = "Update a brazilian state",
            tags = {"Brazilian States"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Brazilian state updated", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Country.class)
                            )}),
                    @ApiResponse(responseCode = "400", description = "Invalid request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )}),
                    @ApiResponse(responseCode = "404", description = "Brazilian state not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )}),
                    @ApiResponse(responseCode = "500", description = "Internal unexpected error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )})
            }
    )
    @RequestMapping(method = RequestMethod.PUT,
            value = BRAZILIAN_STATES,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<EntityModel<BrazilianState>> updateBrazilianState(@RequestBody BrazilianState brazilianState) {

        BrazilianState state = updateBrazilianStateUseCase.updateBrazilianState(UpdateBrazilianStateUseCase.UpdateBrazilianStateCommand
                .builder()
                .brazilianState(brazilianState)
                .build()).getBrazilianState();

        return ResponseEntity.ok(brazilianStateRepresentationModelAssembler
                .toModel(state));
    }

    @Operation(
            operationId = "deleteCountry",
            description = "Delete a brazilian state",
            tags = {"Brazilian States"},
            responses = {
                    @ApiResponse(responseCode = "204", description = "Brazilian state deleted"),
                    @ApiResponse(responseCode = "400", description = "Invalid request", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )}),
                    @ApiResponse(responseCode = "404", description = "Brazilian state not found", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )}),
                    @ApiResponse(responseCode = "500", description = "Internal unexpected error", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)
                            )})
            }
    )
    @RequestMapping(method = RequestMethod.DELETE,
            value = BRAZILIAN_STATES+"/{brazilian-state-id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> deleteCountry(@Parameter(name = "brazilian-state-id", description = "Brazilian state identifier.", required = true, in = ParameterIn.PATH) @PathVariable("brazilian-state-id") Long id) {

        deleteBrazilianStateUseCase.deleteBrazilianState(DeleteBrazilianStateUseCase
                .DeleteBrazilianStateCommand
                .builder()
                .brazilianState(BrazilianState.builder()
                        .id(id)
                        .build())
                .build());

        return ResponseEntity.noContent().build();
    }

}
