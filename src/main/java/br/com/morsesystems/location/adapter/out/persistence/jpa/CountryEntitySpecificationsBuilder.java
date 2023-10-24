package br.com.morsesystems.location.adapter.out.persistence.jpa;

import br.com.morsesystems.location.shared.specification.SearchOperation;
import br.com.morsesystems.location.shared.specification.SpecSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

class CountryEntitySpecificationsBuilder {

    private final List<SpecSearchCriteria> params;

    public CountryEntitySpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public final CountryEntitySpecificationsBuilder with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public final CountryEntitySpecificationsBuilder with(final String orPredicate, final String key, final String operation, final Object value, final String prefix, final String suffix) {
        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) { // the operation may be complex operation
                final boolean startWithAsterisk = prefix != null && prefix.contains(SearchOperation.ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(SearchOperation.ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(orPredicate, key, op, value));
        }
        return this;
    }

    public Specification<CountryJpaEntity> build() {
        if (params.size() == 0)
            return null;

        Specification<CountryJpaEntity> result = new CountryEntitySpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new CountryEntitySpecification(params.get(i)))
                    : Specification.where(result).and(new CountryEntitySpecification(params.get(i)));
        }

        return result;
    }

    public final CountryEntitySpecificationsBuilder with(CountryEntitySpecification spec) {
        params.add(spec.getCriteria());
        return this;
    }

    public final CountryEntitySpecificationsBuilder with(SpecSearchCriteria criteria) {
        params.add(criteria);
        return this;
    }

}
