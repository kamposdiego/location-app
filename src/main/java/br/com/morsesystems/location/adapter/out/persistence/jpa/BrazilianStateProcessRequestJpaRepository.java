package br.com.morsesystems.location.adapter.out.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface BrazilianStateProcessRequestJpaRepository extends JpaRepository<BrazilianStateProcessRequestEntity, String> {
}
