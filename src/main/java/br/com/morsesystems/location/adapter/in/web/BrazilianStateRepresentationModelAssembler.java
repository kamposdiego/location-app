package br.com.morsesystems.location.adapter.in.web;

import br.com.morsesystems.location.domain.BrazilianState;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
class BrazilianStateRepresentationModelAssembler implements RepresentationModelAssembler<BrazilianState, EntityModel<BrazilianState>>{
    @Override
    public EntityModel<BrazilianState> toModel(BrazilianState entity) {
        EntityModel<BrazilianState> brazilianStateEntityModel = EntityModel.of(entity);

        brazilianStateEntityModel.add(linkTo(methodOn(BrazilianStateRestController.class).getBrazilianStateById(entity.getId())).withSelfRel());
        brazilianStateEntityModel.add(linkTo(methodOn(BrazilianStateRestController.class).getBrazilianStates(false, null,null)).withRel(IanaLinkRelations.COLLECTION));

        return brazilianStateEntityModel;

    }
}
