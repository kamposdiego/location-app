package br.com.morsesystems.location.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Builder
@Schema(name = "Country", description = "Domain object used as DTO to represent a Country")
public class Country {

    @Schema(name = "id", description = "Country's id", example = "2", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private Long id;

    @NotBlank
    @NotEmpty
    @Size(min = 3, max = 100)
    @Schema(name = "countryName", description = "Country's name", example = "Canadá", requiredMode = Schema.RequiredMode.REQUIRED)
    private String countryName;

    @NotNull
    @Min(value=1)
    @Max(value=999)
    @Schema(name = "telephoneCodArea", description = "Country's telephone code area", example = "3", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer telephoneCodArea;

}