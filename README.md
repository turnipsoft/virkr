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


