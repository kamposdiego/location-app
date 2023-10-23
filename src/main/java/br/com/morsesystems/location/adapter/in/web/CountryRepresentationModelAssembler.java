package br.com.morsesystems.location.adapter.in.web;

import br.com.morsesystems.location.domain.Country;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class CountryRepresentationModelAssembler implements RepresentationModelAssembler<Country, EntityModel<Country>> {
    
    @Override
    public EntityModel<Country> toModel(Country entity) {
        EntityModel<Country> countryEntityModel = EntityModel.of(entity);

        countryEntityModel.add(linkTo(methodOn(CountryRestController.class).getCountryById(entity.getId())).withSelfRel());
        countryEntityModel.add(linkTo(methodOn(CountryRestController.class).getCountries(false, null,null)).withRel(IanaLinkRelations.COLLECTION));

        return countryEntityModel;
    }

}
