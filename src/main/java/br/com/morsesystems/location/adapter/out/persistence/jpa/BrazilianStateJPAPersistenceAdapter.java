package br.com.morsesystems.location.adapter.out.persistence.jpa;

import br.com.morsesystems.location.application.port.out.DeleteBrazilianStatePort;
import br.com.morsesystems.location.application.port.out.GetBrazilianStatePort;
import br.com.morsesystems.location.application.port.out.SaveBrazilianStatePort;
import br.com.morsesystems.location.application.port.out.UpdateBrazilianStatePort;
import br.com.morsesystems.location.application.exception.InvalidFilterParameterException;
import br.com.morsesystems.location.application.exception.NotFoundException;
import br.com.morsesystems.location.domain.BrazilianState;
import br.com.morsesystems.location.application.util.ExceptionFormatterUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
class BrazilianStateJPAPersistenceAdapter implements GetBrazilianStatePort, SaveBrazilianStatePort, UpdateBrazilianStatePort, DeleteBrazilianStatePort {

    private final BrazilianStateJpaRepository brazilianStateJpaRepository;
    private final SpecifcationFactory specifcationFactory;
    private final ModelMapper modelMapper;

    private BrazilianStateJpaEntity convertToEntity(BrazilianState brazilianState) {
        return modelMapper.map(brazilianState, BrazilianStateJpaEntity.class);
    }

    private BrazilianState convertToDto(BrazilianStateJpaEntity brazilianStateJpaEntity) {
        return modelMapper.map(brazilianStateJpaEntity, BrazilianState.class);
    }

    @Transactional(readOnly = true)
    @Override
    public BrazilianState get(Long brazilianStateId) {
        log.info(String.format("BrazilianState with id %s is in search.", brazilianStateId));

        return this.convertToDto(brazilianStateJpaRepository
                .findById(brazilianStateId).orElseThrow(() -> new NotFoundException("BrazilianState not found.")));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<BrazilianState> getBrazilianStates(Pageable pageable, String filter) {
        try {
            log.info(String.format("Search by brazilian states was performed."));

            Page<BrazilianStateJpaEntity> brazilianStates = brazilianStateJpaRepository.findAll(specifcationFactory
                    .createSpecification(BrazilianStateJpaEntity.class, filter), pageable);

            List<BrazilianState> statesConverted = brazilianStates.getContent().stream().map(this::convertToDto).collect(Collectors.toList());

            return new PageImpl<>(statesConverted, pageable, brazilianStates.getTotalElements());

        } catch (InvalidDataAccessApiUsageException invalidDataAccessApiUsageException){
            throw new InvalidFilterParameterException(ExceptionFormatterUtil
                    .semanticExceptionFormatter(invalidDataAccessApiUsageException.getMessage()));
        }
    }

    @Override
    public BrazilianState save(BrazilianState brazilianState) {

        log.info(String.format("BrazilianState with name %s and IBGE ID %s will be saved.",
                brazilianState.getBrazilianStateName(),
                brazilianState.getBrazilianStateIBGECod()));

        BrazilianStateJpaEntity value = brazilianStateJpaRepository.save(this.convertToEntity(brazilianState));

        log.info(String.format("BrazilianState is saved with name %s and IBGE ID %s, the ID is %s.",
                brazilianState.getBrazilianStateName(),
                brazilianState.getBrazilianStateIBGECod(), value.getId()));

        return this.convertToDto(value);

    }

    @Override
    public BrazilianState update(BrazilianState brazilianState) {
        log.info(String.format("BrazilianState with name %s andIBGE ID %s will be saved.",
                brazilianState.getBrazilianStateName(),
                brazilianState.getBrazilianStateIBGECod()));

        brazilianStateJpaRepository.findById(brazilianState.getId()).orElseThrow(() -> new NotFoundException("BrazilianState not found."));

        BrazilianStateJpaEntity value = brazilianStateJpaRepository.save(this.convertToEntity(brazilianState));

        log.info(String.format("BrazilianState is saved with name %s and IBGE ID %s, the ID is %s.",
                brazilianState.getBrazilianStateName(),
                brazilianState.getBrazilianStateIBGECod(), value.getId()));

        return this.convertToDto(value);
    }

    @Override
    public void delete(Long brazilianStateId) {
        log.info(String.format("BrazilianState with id %s will be deleted.", brazilianStateId));

        brazilianStateJpaRepository.findById(brazilianStateId).orElseThrow(() -> new NotFoundException("BrazilianState not found."));

        brazilianStateJpaRepository.deleteById(brazilianStateId);

        log.info(String.format("BrazilianState with id %s was deleted.", brazilianStateId));
    }

}