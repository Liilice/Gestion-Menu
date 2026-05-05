FROM eclipse-temurin:21

RUN addgroup --system spring && adduser --system --ingroup spring spring

USER spring:spring

ARG DEPENDENCY=build/dependency

COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT ["java","-cp","app:app/lib/*","com.example.backoffice.BackofficeApplication"]