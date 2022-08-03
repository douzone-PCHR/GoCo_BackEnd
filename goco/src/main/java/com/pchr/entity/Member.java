//package com.pchr.entity;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Entity
//@Getter
//@Builder
//@NoArgsConstructor
//public class Member {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    @Column(nullable = false)
//    private String email;
//
//    @Column(nullable = false)
//    private String password;
//
//    @Column(nullable = false)
//    private String nickname;
//
//    @Enumerated(EnumType.STRING)
//    private Authority authority;
//
//    public void setNickname(String nickname) {
//        this.nickname = nickname;
//    }
//
//    public void setPassword(String password) { this.password = password; }
//    
//    
//    public Member(Long id, String email, String password, String nickname, Authority authority) {
//        this.id = id;
//        this.email = email;
//        this.password = password;
//        this.nickname = nickname;
//        this.authority = authority;
//    }
//}