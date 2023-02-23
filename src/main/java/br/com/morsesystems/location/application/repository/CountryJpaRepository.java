package br.com.morsesystems.location.application.repository;

import br.com.morsesystems.location.application.repository.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
* 
* @author Diego Campos
* 
*/
public interface CountryJpaRepository extends JpaRepository<CountryEntity, Long> {

}
