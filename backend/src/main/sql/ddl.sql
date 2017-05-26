-- Table: public.regnskabsdata

-- DROP TABLE public.regnskabsdata;

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
    id bigserial NOT NULL,
    CONSTRAINT regnskabsdata_pkey PRIMARY KEY (id)
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE regnskabsdata
    OWNER to postgres;
