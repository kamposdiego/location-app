package br.com.morsesystems.location.adapter.out.persistence.jpa;

import br.com.morsesystems.location.domain.specification.SearchOperation;
import br.com.morsesystems.location.domain.specification.SpecSearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

class BrazilianStateEntitySpecificationsBuilder {

    private final List<SpecSearchCriteria> params;

    public BrazilianStateEntitySpecificationsBuilder() {
        params = new ArrayList<>();
    }

    public final BrazilianStateEntitySpecificationsBuilder with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public final BrazilianStateEntitySpecificationsBuilder with(final String orPredicate, final String key, final String operation, final Object value, final String prefix, final String suffix) {
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

    public Specification<BrazilianStateEntity> build() {
        if (params.size() == 0)
            return null;

        Specification<BrazilianStateEntity> result = new BrazilianStateEntitySpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new BrazilianStateEntitySpecification(params.get(i)))
                    : Specification.where(result).and(new BrazilianStateEntitySpecification(params.get(i)));
        }

        return result;
    }

    public final BrazilianStateEntitySpecificationsBuilder with(BrazilianStateEntitySpecification spec) {
        params.add(spec.getCriteria());
        return this;
    }

    public final BrazilianStateEntitySpecificationsBuilder with(SpecSearchCriteria criteria) {
        params.add(criteria);
        return this;
    }

}
