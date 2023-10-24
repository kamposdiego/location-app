package br.com.morsesystems.location.adapter.out.persistence.jpa;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "brazilian_states", schema = "public")
@DynamicUpdate
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name = "graph.BrazilianState.complete",
		attributeNodes = @NamedAttributeNode("country"))
class BrazilianStateJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "state_id")
	private Long id;
	
	@Column(name = "state_name", nullable = false)
	private String brazilianStateName;

	@Column(name = "state_ibge_cod", nullable = false)
	private Integer brazilianStateIBGECod;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="country_id", referencedColumnName =  "country_id", nullable=false)
	private CountryJpaEntity country;

	@Column(name = "state_abbreviation", nullable = false)
	private String stateAbbreviation;

}
