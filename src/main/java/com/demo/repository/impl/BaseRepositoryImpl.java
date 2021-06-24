package com.demo.repository.impl;

import com.blazebit.persistence.Criteria;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.spi.CriteriaBuilderConfiguration;
import com.demo.domain.Member;
import com.demo.repository.BaseRepository;
import com.demo.util.PageUtils;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基本儲存庫實作
 *
 * @param <T> 對應的Entity型態
 * @param <K> Entity對應的主鍵型態
 */
@NoRepositoryBean
public class BaseRepositoryImpl<T, K extends Serializable>
        extends SimpleJpaRepository<T, K> implements BaseRepository<T, K> {

    @Getter
    private final EntityManager entityManager;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation,
                              EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    /**
     * @param domainClass   Entity 類別
     * @param entityManager EntityManager
     */
    public BaseRepositoryImpl(Class<T> domainClass, EntityManager entityManager) {
        super(domainClass, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    public List<Tuple> findAllTuple(Specification<T> spec, Pageable pageable) {
        var criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> criteriaQuery = criteriaBuilder.createTupleQuery();
        Root<T> from = criteriaQuery.from(getDomainClass());
        criteriaQuery.where(spec.toPredicate(from, criteriaQuery, criteriaBuilder));
        criteriaQuery.orderBy(QueryUtils.toOrders(pageable.getSort(), from, criteriaBuilder));
        TypedQuery<Tuple> query = entityManager.createQuery(criteriaQuery);
        if (pageable.isPaged()) {
            int firstResult = pageable.getPageNumber() * pageable.getPageSize();
            int maxResults = pageable.getPageSize();
            query.setFirstResult(firstResult).setMaxResults(maxResults);
        }

        return query.getResultList();
    }

    @Override
    public <R> List<R> findAllTuple(
            Specification<T> spec, Pageable pageable, Function<Tuple, R> mapper
    ) {
        return findAllTuple(spec, pageable)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
    }

    @Override
    public List<Tuple> findAllTuple(Specification<T> spec) {
        return findAllTuple(spec, Pageable.unpaged());
    }

    @Override
    public <R> List<R> findAllTuple(Specification<T> spec, Function<Tuple, R> mapper) {
        return findAllTuple(spec, Pageable.unpaged(), mapper);
    }

    @Override
    public Tuple findOneTuple(Specification<T> spec) {
        Pageable pageable = PageRequest.of(0, 1);
        List<Tuple> items = findAllTuple(spec, pageable);
        if (items.isEmpty()) {
            return null;
        } else {
            return items.get(0);
        }
    }

    @Override
    public Page<Tuple> findTuplePage(Specification<T> spec, Pageable pageable) {
        long total = count(spec);
        pageable = PageUtils.refresh(pageable, total);
        List<Tuple> tuples = findAllTuple(spec, pageable);
        return new PageImpl<>(tuples, pageable, total);
    }

    @Override
    public <R> Page<R> findTuplePage(
            Specification<T> spec, Pageable pageable, Function<Tuple, R> mapper
    ) {
        long total = count(spec);
        pageable = PageUtils.refresh(pageable, total);
        List<R> list = findAllTuple(spec, pageable)
                .stream()
                .map(mapper)
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, total);
    }
}
