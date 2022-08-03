//package com.pchr.repository;
//
//import java.util.Optional;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import com.pchr.entity.Member;
//
//@Repository
////email로 Member를 찾는 로직과, email이 존재하는가 판별하는 로직
//public interface MemberRepository extends JpaRepository<Member, Long> {
//    Optional<Member> findByEmail(String email);
//    boolean existsByEmail(String email);
//}