alter table xpd_invoice drop column processed;
CREATE TABLE public.xpd_holiday
(
    id bigint NOT NULL,
    created_date timestamp without time zone,
    holiday_date timestamp without time zone,
    holiday_description character varying(255) COLLATE pg_catalog."default",
    holiday_id character varying(255) COLLATE pg_catalog."default",
    is_national_holiday boolean NOT NULL,
    modified_date timestamp without time zone,
    company_id bigint,
    created_by_id bigint,
    modified_by_id bigint,
    CONSTRAINT xpd_holiday_pkey PRIMARY KEY (id),
    CONSTRAINT fk46jdc7dl69i7jicbk6c04dkax FOREIGN KEY (created_by_id)
        REFERENCES public.xpd_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk9bd6cb68cqshbcviqywpmxdx0 FOREIGN KEY (modified_by_id)
        REFERENCES public.xpd_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkqbdmkrhsj476gn2d36k5x7qy9 FOREIGN KEY (company_id)
        REFERENCES public.xpd_company (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
);
CREATE TABLE public.xpd_holiday_locations
(
    xpd_holiday_id bigint NOT NULL,
    locations_id bigint NOT NULL,
    CONSTRAINT fkbw60my35gfxvm857svj20ac0q FOREIGN KEY (xpd_holiday_id)
        REFERENCES public.xpd_holiday (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkgv76j18m9ipvf2can1o2iy2h FOREIGN KEY (locations_id)
        REFERENCES public.xpd_org_master (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
);
CREATE TABLE public.xpd_payment_date_strategy
(
    id bigint NOT NULL,
    created_date timestamp without time zone,
    minimum_days_gap integer,
    modified_date timestamp without time zone,
    particular_day_of_week character varying(255) COLLATE pg_catalog."default",
    payment_date_strategy character varying(255) COLLATE pg_catalog."default",
    working_days character varying(255) COLLATE pg_catalog."default",
    company_id bigint,
    is_active boolean DEFAULT true,
    created_by_id bigint,
    modified_by_id bigint,
    CONSTRAINT xpd_payment_date_strategy_pkey PRIMARY KEY (id),
    CONSTRAINT fks09s5w46s8e742l7qyygn4awl FOREIGN KEY (modified_by_id)
        REFERENCES public.xpd_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fksd6lxf6qpliagr7xskp3ohvk1 FOREIGN KEY (company_id)
        REFERENCES public.xpd_company (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkxh2036v77oel028hbc0r0qxr FOREIGN KEY (created_by_id)
        REFERENCES public.xpd_user (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
);
alter table xpd_user add column is_xpd_user boolean default false;
alter table xpd_user add column is_data_push boolean default false;
alter table xpd_user add column is_data_pull boolean default false;
alter table xpd_invoice_status add column description character varying(255);
alter table xpd_offer_criteria drop column xpd_offer_id_id;
alter table xpd_user add column is_active boolean default true;
alter table xpd_company add column default_percentage bigint;