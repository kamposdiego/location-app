package br.com.morsesystems.location.application.usecase;

import br.com.morsesystems.location.domain.Country;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public interface CountrySaveLocationUseCase {

    CountrySaveLocationCommand saveCacheLocation(CountrySaveLocationCommand command);

    @Builder
    @RequiredArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @Getter
    class CountrySaveLocationCommand {
        private final Country country;
    }

}
