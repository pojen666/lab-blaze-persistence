package com.demo.repository;

import com.blazebit.persistence.*;
import com.blazebit.persistence.criteria.BlazeCriteria;
import com.blazebit.persistence.criteria.BlazeCriteriaBuilder;
import com.blazebit.persistence.criteria.BlazeCriteriaQuery;
import com.blazebit.persistence.impl.LeafOngoingSetOperationCriteriaBuilderImpl;
import com.demo.domain.Member;
import com.demo.domain.Member_;
import com.demo.domain.Title;
import com.demo.repository.spec.MemberDesignerSpecification;
import com.demo.repository.spec.MemberEngineerSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import javax.persistence.Tuple;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MemberRepositoryTest {

    @Resource
    private MemberRepository memberRepository;

    @Resource
    private CriteriaBuilderFactory criteriaBuilderFactory;

    @Test
    void findById() {
        assertThat(memberRepository.findById(1L)).isNotPresent();
    }

    @Test
    void query() {
        memberRepository.save(buildMember("engineer1", Title.ENGINEER));
        memberRepository.save(buildMember("engineer2", Title.ENGINEER));
        memberRepository.save(buildMember("engineer3", Title.ENGINEER));
        memberRepository.save(buildMember("designer1", Title.DESIGNER));
        memberRepository.save(buildMember("designer2", Title.DESIGNER));
        memberRepository.save(buildMember("manager", Title.MANAGER));
        CriteriaBuilder<Member> memberBuilder = criteriaBuilderFactory.create(memberRepository.getEntityManager(), Member.class);
        List<Member> members = memberBuilder
                .startUnion()
                .from(Member.class)
                .where(Member_.TITLE).eq(Title.ENGINEER)
                .union()
                .from(Member.class)
                .where(Member_.TITLE).eq(Title.DESIGNER)
                .endSet()
                .endSet()
                .getResultList();

        assertThat(members).isNotEmpty();
        BlazeCriteriaBuilder cb = BlazeCriteria.get(criteriaBuilderFactory);
        BlazeCriteriaQuery<Tuple> engineerQuery = cb.createTupleQuery();
        Root<Member> engineerRoot = engineerQuery.from(Member.class);
        engineerQuery.multiselect(
                engineerRoot.get(Member_.no),
                engineerRoot.get(Member_.name)
        );
        engineerQuery.where(
                cb.equal(engineerRoot.get(Member_.title), Title.ENGINEER)
        );

        BlazeCriteriaQuery<Tuple> designerQuery = cb.createTupleQuery();
        Root<Member> root = designerQuery.from(Member.class);
        designerQuery.multiselect(
                root.get(Member_.no),
                root.get(Member_.name)
        );
        designerQuery.where(
                cb.equal(root.get(Member_.title), Title.ENGINEER)
        );

        CriteriaBuilder<Tuple> engineerQueryBuilder = engineerQuery.createCriteriaBuilder(memberRepository.getEntityManager());
        List<Tuple> tuples = engineerQueryBuilder
                .union()
                .from(Member.class)
                .select(Member_.NO)
                .select(Member_.NAME)
                .where(Member_.TITLE).eq(Title.DESIGNER)
                .endSet()
                .getResultList();
        assertThat(tuples).isNotEmpty();
    }

    private Member buildMember(String name, Title title) {
        Member source = new Member();
        source.setName(name);
        source.setTitle(title);
        return source;
    }
}
