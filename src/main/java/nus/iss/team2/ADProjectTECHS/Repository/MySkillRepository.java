package nus.iss.team2.ADProjectTECHS.Repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import nus.iss.team2.ADProjectTECHS.Model.Member;
import nus.iss.team2.ADProjectTECHS.Model.MySkill;


public interface MySkillRepository extends JpaRepository<MySkill, Long>{
    @Override
    Optional<MySkill> findById(Long id);


    List<MySkill> findMySkillsByMember(Member member);

    @Query("select s from MySkill s  join s.member m where m.memberId = :memberId")
    List<MySkill> findMySkillByMemberId(@Param("memberId") Long memberId);
}
