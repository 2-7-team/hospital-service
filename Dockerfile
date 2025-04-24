FROM amazoncorretto:17-alpine
LABEL org.opencontainers.image.authors="박성주 <ilypsj@google.com>"
EXPOSE 19093 # hostpc 포트번호와 매핑될 컨테이너 포트번호 지정
COPY build/libs/hospital-service-0.0.1-SNAPSHOT.jar hospital-service.jar
ENTRYPOINT ["java", "-jar", "hospital-service.jar"]