package br.com.morsesystems.location.adapter.out.persistence.jpa;

import br.com.morsesystems.location.domain.specification.SearchOperation;
import com.google.common.base.Joiner;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
class SpecifcationFactory {

    private Matcher getMatcher(String filter){
        String operationSetExper = Joiner.on("|")
                .join(SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile("(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),");
        Matcher matcher = pattern.matcher(filter + ",");

        return matcher;
    }

    public <T> Specification createSpecification(Class<T> type, String filter) {
        switch (type.getSimpleName()) {
            case "CountryJpaEntity":
                CountryEntitySpecificationsBuilder countryEntitySpecificationsBuilder = new CountryEntitySpecificationsBuilder();

                Matcher matcherCountryEntity = getMatcher(filter);

                while (matcherCountryEntity.find()) {
                    countryEntitySpecificationsBuilder.with(matcherCountryEntity.group(1), matcherCountryEntity.group(2), matcherCountryEntity.group(4), matcherCountryEntity.group(3), matcherCountryEntity.group(5));
                }

                return countryEntitySpecificationsBuilder.build();

            case "BrazilianStateJpaEntity":
                BrazilianStateEntitySpecificationsBuilder brazilianStateEntitySpecificationsBuilder = new BrazilianStateEntitySpecificationsBuilder();

                Matcher matcherBrazilianState = getMatcher(filter);

                while (matcherBrazilianState.find()) {
                    brazilianStateEntitySpecificationsBuilder.with(matcherBrazilianState.group(1), matcherBrazilianState.group(2),
                            matcherBrazilianState.group(4), matcherBrazilianState.group(3), matcherBrazilianState.group(5));
                }

                return brazilianStateEntitySpecificationsBuilder.build();

            default:
                throw new UnsupportedOperationException("Nao e possivel criar specification para a entidade " + type.getClass());
        }
    }

}
