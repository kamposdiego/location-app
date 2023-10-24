package br.com.morsesystems.location.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Builder
@Schema(name = "BrazilianState", description = "Domain object used as DTO to represent a BrazilianState")
public class BrazilianState implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(name = "id", description = "BrazilianState's id", example = "1", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long id;

    @NotBlank
    @NotEmpty
    @Size(min = 4, max = 100)
    @Schema(name = "brazilianStateName", description = "BrazilianState's name", example = "São Paulo", requiredMode = Schema.RequiredMode.REQUIRED)
    private String brazilianStateName;

    @NotNull
    @Min(value=1)
    @Max(value=999)
    @Schema(name = "brazilianStateIBGECod", description = "BrazilianState's IBGE code", example = "35", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer brazilianStateIBGECod;

    @NotNull
    @Schema(name = "country", requiredMode = Schema.RequiredMode.REQUIRED)
    private Country country;

    @NotBlank
    @NotEmpty
    @Size(min = 2, max = 2)
    @Schema(name = "stateAbbreviation", description = "BrazilianState's abbreviation", example = "SP", requiredMode = Schema.RequiredMode.REQUIRED)
    private String stateAbbreviation;
}
