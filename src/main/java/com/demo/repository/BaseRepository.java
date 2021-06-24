package com.demo.repository;

import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.demo.domain.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

/**
 * 基本儲存庫
 *
 * @param <T> entity
 * @param <K> 主鍵
 */
@NoRepositoryBean
public interface BaseRepository<T, K extends Serializable>
        extends JpaRepositoryImplementation<T, K> {

    List<Tuple> findAllTuple(Specification<T> spec, Pageable pageable);

    <R> List<R> findAllTuple(
            Specification<T> spec, Pageable pageable, Function<Tuple, R> mapper
    );

    List<Tuple> findAllTuple(Specification<T> spec);

    <R> List<R> findAllTuple(Specification<T> spec, Function<Tuple, R> mapper);

    Tuple findOneTuple(Specification<T> spec);

    Page<Tuple> findTuplePage(Specification<T> spec, Pageable pageable);

    <R> Page<R> findTuplePage(
            Specification<T> spec, Pageable pageable, Function<Tuple, R> mapper
    );

    EntityManager getEntityManager();
}
