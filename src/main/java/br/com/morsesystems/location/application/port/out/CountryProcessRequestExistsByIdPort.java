package br.com.morsesystems.location.application.port.out;

public interface CountryProcessRequestExistsByIdPort {

    Boolean existsById(String xIdempotencyKey);

}
