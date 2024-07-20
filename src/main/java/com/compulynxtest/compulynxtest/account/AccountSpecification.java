package com.compulynxtest.compulynxtest.account;

import org.springframework.data.jpa.domain.Specification;

public class AccountSpecification {

    public static Specification<Account> withCustomerId(Long customerId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("customer").get("id"), customerId);
    }
}
