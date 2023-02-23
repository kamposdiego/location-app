package br.com.morsesystems.location.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryLocationEvent {
        private String idempotencyKey;
        private Long id;
        private String countryName;
        private Integer telephoneCodArea;

}
