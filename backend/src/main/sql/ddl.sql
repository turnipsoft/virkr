-- Table: public.regnskabsdata

DROP TABLE public.regnskabsdata;

CREATE TABLE regnskabsdata
(
    cvrnummer character varying(8) COLLATE pg_catalog."default" NOT NULL,
    startdato date NOT NULL,
    slutdato date NOT NULL,
    sidsteopdatering date NOT NULL,
    omgoerelse boolean,
    pdfurl character varying(1024) COLLATE pg_catalog."default",
    xbrlurl character varying(1024) COLLATE pg_catalog."default",
    bruttofortjeneste bigint,
    driftsresultat bigint,
    aaretsresultat bigint,
    resultatfoerskat bigint,
    skatafaaretsresultat bigint,
    gaeldsforpligtelser bigint,
    egenkapital bigint,
    finansielleindtaegter bigint,
    finansielleomkostninger bigint,
    medarbejderomkostninger bigint,
    omsaetning bigint,
    navn character varying(255) COLLATE pg_catalog."default",
    vejnavn character varying(512) COLLATE pg_catalog."default",
    husnr character varying(20) COLLATE pg_catalog."default",
    postnr character varying(10) COLLATE pg_catalog."default",
    bynavn character varying(255) COLLATE pg_catalog."default",
    lat character varying(20) COLLATE pg_catalog."default",
    lon character varying(20) COLLATE pg_catalog."default",
    id bigserial NOT NULL,
    CONSTRAINT regnskabsdata_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE regnskabsdata
    OWNER to postgres;

CREATE TABLE virksomhedsdata
(
    cvrnummer character varying(8) COLLATE pg_catalog."default" NOT NULL,
    navn character varying(255) COLLATE pg_catalog."default",
    vejnavn character varying(512) COLLATE pg_catalog."default",
    husnr character varying(20) COLLATE pg_catalog."default",
    postnr character varying(10) COLLATE pg_catalog."default",
    bynavn character varying(255) COLLATE pg_catalog."default",
    lat character varying(20) COLLATE pg_catalog."default",
    lon character varying(20) COLLATE pg_catalog."default",
    CONSTRAINT virksomhedsdata_pkey PRIMARY KEY (cvrnummer)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE virksomhedsdata
    OWNER to postgres;

