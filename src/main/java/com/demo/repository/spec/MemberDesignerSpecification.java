package com.demo.repository.spec;

import com.demo.domain.Member;
import com.demo.domain.Member_;
import com.demo.domain.Title;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class MemberDesignerSpecification implements Specification<Member> {

    @Override
    public Predicate toPredicate(Root<Member> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(root.get(Member_.title), Title.DESIGNER));
        return builder.and(predicates.toArray(new Predicate[]{}));
    }
}
