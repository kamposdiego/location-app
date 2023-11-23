package br.com.morsesystems.location.adapter.out.persistence.jpa;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
public class SpecifcationFactoryTest {

    @SpyBean
    private SpecifcationFactory specifcationFactory;

    @Test
    void givenAnotherClass_whenCreate_shouldReturnUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> specifcationFactory.createSpecification(Object.class, "countryName:Brasil"));
    }

    @Test
    void givenCountryClassWithFilterEqual_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(CountryJpaEntity.class, "countryName:Brasil"));
    }

    @Test
    void givenCountryClassWithFilterNegation_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(CountryJpaEntity.class, "countryName!Brasil"));
    }

    @Test
    void givenCountryClassWithFilterGreaterThan_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(CountryJpaEntity.class, "id>2"));
    }

    @Test
    void givenCountryClassWithFilterLessThan_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(CountryJpaEntity.class, "id<10"));
    }

    @Test
    void givenCountryClassWithFilterLike_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(CountryJpaEntity.class, "countryName~Bra"));
    }

    @Test
    void givenCountryClassWithFilterInvalid_whenCreate_shouldReturnSpecifcation() {
        assertNull(specifcationFactory.createSpecification(CountryJpaEntity.class, "countryName#Bra"));
    }

    @Test
    void givenBrazilianStateClassWithFilterEqual_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(BrazilianStateJpaEntity.class, "brazilianStateName:Alagoas"));
    }

    @Test
    void givenBrazilianStateClassWithFilterNegation_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(BrazilianStateJpaEntity.class, "brazilianStateName!Alagoas"));
    }

    @Test
    void givenBrazilianStateClassWithFilterGreaterThan_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(BrazilianStateJpaEntity.class, "id>2"));
    }

    @Test
    void givenBrazilianStateClassWithFilterLessThan_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(BrazilianStateJpaEntity.class, "id<10"));
    }

    @Test
    void givenBrazilianStateClassWithFilterLike_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(BrazilianStateJpaEntity.class, "brazilianStateName~Ala"));
    }

    @Test
    void givenBrazilianStateClassWithFilterStartWith_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(BrazilianStateJpaEntity.class, "brazilianStateName:*ala"));
    }

    @Test
    void givenBrazilianStateClassWithFilterEndsWith_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(BrazilianStateJpaEntity.class, "brazilianStateName:ala*"));
    }

    @Test
    void givenBrazilianStateClassWithFilterContains_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(BrazilianStateJpaEntity.class, "brazilianStateName:*ala*"));
    }

    @Test
    void givenBrazilianStateClassWithFilterInvalid_whenCreate_shouldReturnSpecifcation() {
        assertNull(specifcationFactory.createSpecification(BrazilianStateJpaEntity.class, "brazilianStateName#Ala"));
    }

}
