package br.com.morsesystems.location.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BrazilianStateProcessRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String xIdempotencyKey;

    private LocalDateTime processDateTime;

}
