package com._7.bookinghospital.hospital_service.infrastructure.repository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Component
@Slf4j
public class UserInfoAuditorAware implements AuditorAware<Long> {
    private static final Long DEFAULT_AUDITOR = -1L;

    @Override
    public Optional<Long> getCurrentAuditor() {
        try{
            ServletRequestAttributes attrs =
                    (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();

            if(attrs == null) {
                log.debug("요청 컨텍스트가 존재하지 않습니다.");
                return Optional.of(DEFAULT_AUDITOR);
            }

            HttpServletRequest request = attrs.getRequest();
            String userId = request.getHeader("X-User-Name");
            log.info("UserInfoAuditorAware.java - userId: {}", userId);

            // if(userId == null || userId.isBlank())
            if(!StringUtils.hasText(userId)) {
                log.debug("요청 헤더에 X-User-Name이 존재하지 않습니다.");
                return Optional.of(DEFAULT_AUDITOR);
            }

            return Optional.of(Long.valueOf(userId));
        } catch (Exception e) {
            log.warn("정보 조회 중 오류 발생: ", e);
            return Optional.of(DEFAULT_AUDITOR);
        }
    }
}
