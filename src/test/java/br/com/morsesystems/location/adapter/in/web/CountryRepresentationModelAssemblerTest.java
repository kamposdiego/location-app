package br.com.morsesystems.location.adapter.in.web;

import br.com.morsesystems.location.adapter.in.web.CountryRepresentationModelAssembler;
import br.com.morsesystems.location.domain.Country;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.EntityModel;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CountryRepresentationModelAssemblerTest {

    @Test
    void toModel_whenCountryIsNotNull_shouldReturnCountryModel() {

        CountryRepresentationModelAssembler countryRepresentationModelAssembler = new CountryRepresentationModelAssembler();

        EntityModel<Country> entityCountry = countryRepresentationModelAssembler.toModel(Country.builder()
                .id(1L)
                .countryName("Brazil")
                .telephoneCodArea(55)
                .build());

        assertTrue(entityCountry.getLinks().hasLink("self"));
        assertTrue(entityCountry.getLinks().hasLink("collection"));
        assertEquals("/countries/1", entityCountry.getLinks().getLink("self").get().getHref());
        assertEquals("/countries?includeDeleted=false{&filter}", entityCountry.getLinks().getLink("collection").get().getHref());

    }

}
