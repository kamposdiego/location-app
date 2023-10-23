package br.com.morsesystems.location.domain;

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
public class BrazilianState implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotBlank
    @NotEmpty
    @Size(min = 4, max = 100)
    private String brazilianStateName;

    @NotNull
    @Min(value=1)
    @Max(value=999)
    private Integer brazilianStateIBGECod;

    @NotNull
    private Country country;

    @NotBlank
    @NotEmpty
    @Size(min = 2, max = 2)
    private String stateAbbreviation;
}
