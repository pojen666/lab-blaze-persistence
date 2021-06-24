package com.demo.domain.builder;

import com.blazebit.persistence.ObjectBuilder;
import com.blazebit.persistence.SelectBuilder;
import com.demo.domain.Member_;
import org.apache.commons.lang3.tuple.Pair;
import org.hibernate.jpa.spi.TupleBuilderTransformer;

import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import java.util.List;

public class MemberNoAndNameBuilder implements ObjectBuilder<Tuple> {

    @Override
    public <X extends SelectBuilder<X>> void applySelects(X queryBuilder) {
        queryBuilder.select(Member_.NO).select(Member_.NAME);
    }

    @Override
    public Tuple build(Object[] objects) {
        return null;
    }

    @Override
    public List<Tuple> buildList(List<Tuple> list) {
        return list;
    }
}
