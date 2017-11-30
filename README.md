# virkr
Simpel virksomhedsinfo portal

## Projektstruktur
Projektet er opdelt i tre dele: frontend, backend og test. Der er gradle tasks, som virker i begge projekter:

    ./gradlew clean

Sletter build i backendprojektet, sletter build og node_modules i frontend projektet

    ./gradlew build

Bygger backend og frontend projektet. Distributables i backend/libs/virk-0.01.jar og frontend/build

For at starte frontend og backend:

I et vindue: `./gradlew backend:start`
I et andet vindue: `./gradlew frontend:start`      

Hvis frontend ikke starter første gang så kør `npm install` i frontend  

## Komponenter i løsningen

### Backend
Backenden består af en Spring Boot applikation som varetager kommunikation med eksterne services herunder: 
- ERST Offentliggørelsesindeks, herfra hentes xbrl regnskaber som er hele baggrunden for løsningen
- ERST CVR indeks, herfra hentes stamoplysninger om virksomheden ligesom indekset anvendes til søgning på virksomhedsnavne
- Google MAPS API, anvendes til geokodning af adresser. Dette anvendes dog p.t. ikke.

Backenden henter regnskaber ved forespørgsel på en virksomheds regnskaber, parser disse for at finde nøgletal og 
persisterer dernæst nøgletallene til næste gang nogle forespørger på nøgletal for denne virksomhed
Dette er lavet af performance hensyn, da det er relativt dyrt at parse regnskaberne on the fly. Det er acceptabelt
men det giver en langt bedre brugeroplevelser når de bare kommer med det samme.

Det betyder også at regnskabernes nøgletal forsøges at blive opdateret af et batchjob, når det er sandsynligt der er kommet et nyt regnskab.

Når regnskaber parses, findes først alle de nøgletal programmet kender og ud fra disse nøgletal forsøges der at finde nøgletal
som ikke nødvendigvis står i XBRL'en men som kan udregnes ud fra andre nøgletal.

Data persisteres p.t. i en postgres database, men det er subject to change, det er muligvis mere optimalt med en dokument
database.

#### Start af backend
Backenden bygges som en executable jar og kan derfor startes helt simpelt med ./virkr.jar eller man kan lægge den ind /etc/init.d 
og dermed have den som service. 
Backenden skal dog have passwords og apikey som parametre da disse ikke står i config filen,
Det kan enten gøres ved : 

./virkr.jar --virkr.maps.apikey=MAPS-APIKEY --virkr.cvr.password=CVR-PASS

Eller man kan give en ekstern config file location .

./virkr.jar --spring.config.localtion=file://stitilkonfiguration

#### JSON REST Endpoints
Backenden udstiller 3 restendpoints :
- /regnskab/<cvrnr> : Henter regnskabsnøgletal for den pågædende virksomhed
- /cvr/<cvrnr> : Henter virksomheds stamoplysninger for den pågældende virksomhed
- /cvr/search/<soegning> : Foretager en søgning på virksomhedr i CVR Indeks

#### Docker
For at bygge en container med Tomcat 7 (java 7) og applikationen deployed:

    docker build -t virkr/erst .

efterfulgt af:

    docker run --name virkr -d -p 8080:8080 virkr/erst

Apllikationen er herefter tilgængelig på http://localhost:8080/virkr

Loggen kan læses med:
   
    docker logs -f virkr

Containeren stoppes igen med:

    docker stop virkr


### Frontenden
Frontenden er lavet som en REACT applikation og er en SPA, udover REACT anvendes FLUX som design pattern for kommunikation
mellem komponenten. Fetch anvendes som AJAX api.

Applikationen er konfigureret med webpack og installeres lokalt med npm install

Applikationen er struktureret i forhold til FLUX , så alle React komponenter bor i hhv:
- containers/
- stores/
- views/
- actions.js
- dispatcher.js

