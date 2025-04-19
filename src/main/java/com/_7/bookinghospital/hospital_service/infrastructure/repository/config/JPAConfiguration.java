package com._7.bookinghospital.hospital_service.infrastructure.repository.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// QueryDSL 기술을 사용하기 위해 JPAQueryFactory 를 사용하기 위해 설정하는 파일
@Configuration
public class JPAConfiguration {
    @PersistenceContext
    private EntityManager em;

    // JPAQueryFactory 빈 생성
    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(this.em);
    }
}
