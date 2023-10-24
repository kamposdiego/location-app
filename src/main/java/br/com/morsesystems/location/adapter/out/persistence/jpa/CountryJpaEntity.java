package br.com.morsesystems.location.adapter.out.persistence.jpa;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.io.Serializable;

@Entity
@Table(name = "country", schema = "public")
@DynamicUpdate
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountryJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "country_id")
	private Long id;
	
	@Column(name = "country_name", nullable = false)
	private String countryName;
	
	@Column(name = "telephone_code_area", nullable = false)
	private Integer telephoneCodArea;

}
