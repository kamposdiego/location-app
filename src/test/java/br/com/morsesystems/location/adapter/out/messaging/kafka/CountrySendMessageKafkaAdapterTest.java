package br.com.morsesystems.location.adapter.out.messaging.kafka;

import br.com.morsesystems.location.adapter.out.messaging.kafka.CountryEvent;
import br.com.morsesystems.location.adapter.out.messaging.kafka.CountrySendMessageKafkaAdapter;
import br.com.morsesystems.location.domain.Country;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cloud.stream.function.StreamBridge;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
public class CountrySendMessageKafkaAdapterTest {

    @InjectMocks
    private CountrySendMessageKafkaAdapter countrySendMessageKafkaAdapter;

    @Mock
    private StreamBridge streamBridge;

    @Test
    void givenCountry_whenSendMessage_shouldReturnVoid() {
        given(streamBridge.send(anyString(), any(CountryEvent.class))).willReturn(true);

        countrySendMessageKafkaAdapter.sendMessage(Country
                .builder()
                        .id(1L)
                        .countryName("Brazil")
                        .telephoneCodArea(55)
                .build());

        then(streamBridge).should().send(anyString(), any(CountryEvent.class));
    }
}
