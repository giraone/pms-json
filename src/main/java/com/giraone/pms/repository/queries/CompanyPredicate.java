package com.giraone.pms.repository.queries;

import com.giraone.pms.domain.Company;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;

public class CompanyPredicate {

    private SearchCriteria criteria;

    public CompanyPredicate(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    public BooleanExpression getPredicate() {

        PathBuilder<Company> entityPath = new PathBuilder<>(Company.class, "company");
        StringPath path = entityPath.getString(criteria.getKey());
        if (criteria.getOperation().equalsIgnoreCase(":")) {
            return path.containsIgnoreCase(criteria.getValue().toString());
        }
        return null;
    }
}
