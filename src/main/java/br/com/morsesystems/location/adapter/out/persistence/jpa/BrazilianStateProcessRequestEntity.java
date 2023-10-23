package br.com.morsesystems.location.adapter.out.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "brazilian_states_process", schema = "public")
@DynamicUpdate
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
class BrazilianStateProcessRequestEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "x_idempotency_key")
    private String xIdempotencyKey;

    @Column(name = "process_date_time", nullable = false)
    private LocalDateTime processDateTime;

}
