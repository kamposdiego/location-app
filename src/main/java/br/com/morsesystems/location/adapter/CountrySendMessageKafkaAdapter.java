package br.com.morsesystems.location.adapter;

import br.com.morsesystems.location.api.CountryLocationEvent;
import br.com.morsesystems.location.application.port.CountrySendMessagePort;
import br.com.morsesystems.location.domain.Country;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CountrySendMessageKafkaAdapter implements CountrySendMessagePort {

    private static final String COUNTRY_PRODUCER = "producer-out-0";

    private final StreamBridge streamBridge;

    public void sendMessage(Country country){
        String idempotencyKey = UUID.randomUUID().toString();

        log.info(String.format("The idempotencyKey %s was generated.",
                idempotencyKey));

        log.info(String.format("Country with id %s name %s and DDI %s will be send.",
                country.getId(),
                country.getCountryName(),
                country.getTelephoneCodArea()));

        streamBridge.send(COUNTRY_PRODUCER, CountryLocationEvent.builder()
                .idempotencyKey(idempotencyKey)
                .id(country.getId())
                .countryName(country.getCountryName())
                .telephoneCodArea(country.getTelephoneCodArea())
                .build()
        );

        log.info(String.format("Country with id %s name %s and DDI %s was sent to output binding %s with idempotencyKey %s.",
                country.getId(),
                country.getCountryName(),
                country.getTelephoneCodArea(),
                COUNTRY_PRODUCER,
                idempotencyKey));

    }
}
