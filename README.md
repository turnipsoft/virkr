# virkr
Simpel virksomhedsinfo portal

## Projektstruktur
Projektet er opdelt i to dele: frontend og backend. Der er gradle tasks, som virker i begge projekter:

    ./gradlew clean

Sletter build i backendprojektet, sletter build og node_modules i frontend projektet

    ./gradlew build

Bygger backend og frontend projektet. Distributables i backend/libs/virk-0.01.jar og frontend/build

For at starte frontend og backend:

I et vindue: `./gradlew backend:start`
I et andet vindue: `./gradlew frontend:start`      

Hvis frontend ikke starter første gang så kør `npm install` i frontend  

Hvis vi kan kunne finde ud af at få gradle til at afvikle to tasks parallelt kunne man nøjes med `./gradlew start` 


## Postgresql og Docker
Postgresql kan startes med Docker ved i roden af projektet skrive:

    build -t virkr/postgres .

efterfulgt af:

    docker run --name virkrdb -d -p 5432:5432 virkr/postgres

Postgresql er herefter oppe på localhost:5432.

Databasen bliver automatisk opsat med indholdet af `backend/src/main/sql/ddl.sql`        
