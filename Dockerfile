# 1단계 : 빌드 스테이지

# 기본 이미지를 정하는 부분
FROM amazoncorretto:17 AS builder

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 파일 먼저 복사
COPY gradle ./gradle
COPY gradlew ./gradlew

# gradlew 파일에 실행 권한 부여
RUN chmod +x ./gradlew

# Gradle 캐시를 위한 의존성 파일 복사
COPY build.gradle settings.gradle ./

# Gradle 의존성 미리 다운로드
RUN ./gradlew dependencies --no-daemon

# 소스 코드 복사 및 빌드
COPY src ./src

# 애플리케이션 빌드
RUN ./gradlew bootJar -x test --no-daemon # 테스트 미포함
#RUN #./gradlew build # 테스트 포함함! 빌드 느림!!

# -------------------------------------------------------------------------
# 2단계 : 런타임 스테이지

# 아래는 Java 17이 포함된 Alpine 기반 이미지
FROM amazoncorretto:17-alpine3.21

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 정보 환경 변수
ENV PROJECT_NAME=discodeit
ENV PROJECT_VERSION=1.2-M8

# JVM 옵션 환경 변수
ENV JVM_OPTS=""

# Spring Boot 실행 포트
ENV SERVER_PORT=80

# 빌드된 jar 파일 복사
COPY --from=builder /app/build/libs/${PROJECT_NAME}-${PROJECT_VERSION}.jar /app/${PROJECT_NAME}-${PROJECT_VERSION}.jar

# 80 포트 노출
EXPOSE 80

# 애플리케이션 실행
ENTRYPOINT ["sh", "-c", "java $JVM_OPTS -jar /app/${PROJECT_NAME}-${PROJECT_VERSION}.jar"]