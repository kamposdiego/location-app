package br.com.morsesystems.location.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
interface CountryJpaRepository extends JpaRepository<CountryJpaEntity, Long>, JpaSpecificationExecutor<CountryJpaEntity> {

}
