package br.com.morsesystems.location.adapter.out.messaging.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
class CountryEvent {

        private String idempotencyKey;
        private Long id;
        private String countryName;
        private Integer telephoneCodArea;

}
