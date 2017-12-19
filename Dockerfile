FROM tomcat:alpine

ENV SPRING_PROFILES_ACTIVE=ERST VIRKR_MAPS_APIKEY==AIzaSyCFIFw8X7OTSOJ5lh-Z-HUxzNzbD2TYl-w VIRKR_CVR_PASSWORD=aa2d9502-86b1-4b08-b874-fd10960df469
    
WORKDIR .

COPY ./backend/build/libs/virkr-0.0.1.war /usr/local/tomcat/webapps/virkr.war
