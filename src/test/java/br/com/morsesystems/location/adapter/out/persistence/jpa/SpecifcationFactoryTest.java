package br.com.morsesystems.location.adapter.out.persistence.jpa;

import br.com.morsesystems.location.adapter.out.persistence.jpa.BrazilianStateEntity;
import br.com.morsesystems.location.adapter.out.persistence.jpa.CountryEntity;
import br.com.morsesystems.location.adapter.out.persistence.jpa.SpecifcationFactory;
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
        assertNotNull(specifcationFactory.createSpecification(CountryEntity.class, "countryName:Brasil"));
    }

    @Test
    void givenCountryClassWithFilterNegation_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(CountryEntity.class, "countryName!Brasil"));
    }

    @Test
    void givenCountryClassWithFilterGreaterThan_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(CountryEntity.class, "id>2"));
    }

    @Test
    void givenCountryClassWithFilterLessThan_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(CountryEntity.class, "id<10"));
    }

    @Test
    void givenCountryClassWithFilterLike_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(CountryEntity.class, "countryName~Bra"));
    }

    @Test
    void givenCountryClassWithFilterInvalid_whenCreate_shouldReturnSpecifcation() {
        assertNull(specifcationFactory.createSpecification(CountryEntity.class, "countryName#Bra"));
    }

    @Test
    void givenBrazilianStateClassWithFilterEqual_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(BrazilianStateEntity.class, "brazilianStateName:Alagoas"));
    }

    @Test
    void givenBrazilianStateClassWithFilterNegation_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(BrazilianStateEntity.class, "brazilianStateName!Alagoas"));
    }

    @Test
    void givenBrazilianStateClassWithFilterGreaterThan_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(BrazilianStateEntity.class, "id>2"));
    }

    @Test
    void givenBrazilianStateClassWithFilterLessThan_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(BrazilianStateEntity.class, "id<10"));
    }

    @Test
    void givenBrazilianStateClassWithFilterLike_whenCreate_shouldReturnSpecifcation() {
        assertNotNull(specifcationFactory.createSpecification(BrazilianStateEntity.class, "brazilianStateName~Ala"));
    }

    @Test
    void givenBrazilianStateClassWithFilterInvalid_whenCreate_shouldReturnSpecifcation() {
        assertNull(specifcationFactory.createSpecification(BrazilianStateEntity.class, "brazilianStateName#Ala"));
    }

}
