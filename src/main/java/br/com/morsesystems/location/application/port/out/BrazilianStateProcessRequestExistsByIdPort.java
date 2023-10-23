package br.com.morsesystems.location.application.port.out;

public interface BrazilianStateProcessRequestExistsByIdPort {

    Boolean existsById(String xIdempotencyKey);

}
