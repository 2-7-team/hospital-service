병원 마이크로서비스
===

#### TODO

##### 병원 생성 API 구현
- [x] 병원 도메인 설계
  - 병원 식별자(id, UUID)
  - 병원 이름(name, String)
  - 병원 주소(address, String)
  - 병원 전화번호(phone, String)
  - 병원 소개글(description, String)
  - 병원 영업 오픈 시각(openHour, LocalTime)
  - 병원 영업 마감 시각(closeHour, LocalTime)
  - 병원 생성 일시(createdAt, LocalDateTime)
  - 병원 수정 일시(updatedAt, LocalDateTime)
  - 병원 생성자(createdBy, Long)
  - 병원 수정자(updatedBy, Long)
  - 병원 삭제 여부(isDeleted, boolean)
- [x] 병원 JPA 엔티티를 정의
  - @Entity, @NoArgsConstructor(access = AccessLevel.PROTECTED)
- [x] 병원 영속 처리를 위한 JPA Repository 정의
  - extends JpaRepository<Hospital, UUID>
- [x] 4계층 구조로 설계
  - **presentation** : *Controller.java, *Dto.java
  - **application**: *Service.java
  - **domain**: Entity, Repository<Interface → DIP>
  - **infrastructure**: JpaRepository, *RepositoryImpl, *RepositoryAdapter
- [x] 병원 생성 서비스 구현
- [x] 병원 생성 API 구현
  - 요청
    - POST /api/hospitals
    - 병원 이름, 병원 주소, 병원 전화번호, 병원 소개, 병원 영업 오픈 시각, 병원 영업 마감 시각
  - 응답
    - 201 CREATED
    - Header → Location 필드에 생성된 병원을 조회할 수 있는 URI 를 구성