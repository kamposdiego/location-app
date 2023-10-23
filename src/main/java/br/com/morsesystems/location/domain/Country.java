package br.com.morsesystems.location.domain;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = false)
@Builder
public class Country {

    private Long id;

    @NotBlank
    @NotEmpty
    @Size(min = 3, max = 100)
    private String countryName;

    @NotNull
    @Min(value=1)
    @Max(value=999)
    private Integer telephoneCodArea;

}