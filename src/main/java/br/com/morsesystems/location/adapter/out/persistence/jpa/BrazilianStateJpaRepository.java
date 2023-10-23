package br.com.morsesystems.location.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

interface BrazilianStateJpaRepository extends JpaRepository<BrazilianStateEntity, Long>, JpaSpecificationExecutor<BrazilianStateEntity> {
}
