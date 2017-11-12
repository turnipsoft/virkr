FROM library/postgres:alpine
ADD backend/src/main/sql/ddl.sql /docker-entrypoint-initdb.d/
ENV POSTGRES_USER postgres
ENV POSTGRES_DB postgres
EXPOSE 5432

