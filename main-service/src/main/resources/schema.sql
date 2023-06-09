drop table if exists EVENTS_COMPILATIONS, EVENTS, EVENT_REQUESTS, COMPILATIONS, CATEGORIES, LOCATION, USERS, STATS, SUBSCRIBERS cascade;

create table STATS
(
    STAT_ID   BIGINT generated by default as identity not null
        primary key,
    APP       VARCHAR(50) not null,
    URI       VARCHAR(50) not null,
    IP        VARCHAR(50) not null,
    TIMESTAMP TIMESTAMP without time zone not null
);

create table if not exists CATEGORIES
(
    CATEGORY_ID BIGINT generated by default as identity not null,
    NAME        VARCHAR(100) not null,
    constraint UQ_CATEGORY_NAME UNIQUE (NAME),
    constraint CATEGORIES_PK
        primary key (CATEGORY_ID)
);

create table if not exists LOCATION
(
    LOCATION_ID BIGINT generated by default as identity not null
        primary key,
    LAT         DOUBLE PRECISION not null,
    LON         DOUBLE PRECISION not null
);

create table if not exists USERS
(
    USER_ID BIGINT generated by default as identity not null,
    EMAIL   VARCHAR(150) not null unique,
    NAME    VARCHAR(150) not null,
    SUBSCRIBE          BOOLEAN               default 'TRUE'    not null,
    constraint ID
        primary key (USER_ID)
);

create table if not exists EVENTS
(
    EVENT_ID           BIGINT generated by default as identity not null
        primary key,
    ANNOTATION         VARCHAR                   not null,
    CATEGORY           BIGINT                              not null,
    CREATED_ON         TIMESTAMP without time zone,
    DESCRIPTION        VARCHAR,
    EVENT_DATE         TIMESTAMP without time zone         not null,
    INITIATOR          BIGINT                              not null,
    LOCATION           BIGINT                              not null,
    PAID               BOOLEAN                             not null,
    PARTICIPANT_LIMIT  INTEGER           default 0         not null,
    PUBLISHED_ON       TIMESTAMP without time zone,
    REQUEST_MODERATION BOOLEAN           default TRUE      not null,
    STATE              VARCHAR(10) default 'PENDING' not null,
    TITLE              VARCHAR                   not null,
    constraint EVENTS_CATEGORIES_CATEGORY_ID_FK
        foreign key (CATEGORY) references CATEGORIES,
    constraint EVENTS_LOCATION_LOCATION_ID_FK
        foreign key (LOCATION) references LOCATION,
    constraint EVENTS_USERS_USER_ID_FK
        foreign key (INITIATOR) references USERS
);

create table if not exists EVENT_REQUESTS
(
    REQUEST_ID        BIGINT generated by default as identity not null,
    CREATED   TIMESTAMP without time zone not null,
    EVENT     BIGINT            not null,
    REQUESTER BIGINT            not null,
    STATUS    VARCHAR(10) not null,
    constraint EVENT_REQUESTS_PK
        primary key (REQUEST_ID),
    constraint REQUESTS_EVENTS_EVENT_ID_FK
        foreign key (EVENT) references EVENTS on delete cascade,
    constraint REQUESTS_USERS_USER_ID_FK
        foreign key (REQUESTER) references USERS on delete cascade
);

create table if not exists COMPILATIONS
(
    COMPILATION_ID   BIGINT generated by default as identity not null
        primary key,
    COMPILATION_NAME VARCHAR(100) not null,
    PINNED           BOOLEAN           not null,
    constraint UQ_COMPILATION_NAME UNIQUE (COMPILATION_NAME)
);

create table if not exists EVENTS_COMPILATIONS
(
    EVENTS_COMPILATIONS_ID BIGINT generated by default as identity not null
        primary key,
    EVENT_ID               BIGINT not null,
    COMPILATION_ID         BIGINT not null,
    constraint EVENTS_COMPILATIONS_COMPILATIONS_COMPILATION_ID_FK
        foreign key (COMPILATION_ID) references COMPILATIONS on delete cascade,
    constraint EVENTS_COMPILATIONS_EVENTS_EVENT_ID_FK
        foreign key (EVENT_ID) references EVENTS on delete cascade
);

create table if not exists SUBSCRIBERS
(
    ID      BIGINT generated by default as identity not null
        primary key,
    USER_ID BIGINT not null,
    SUB_ID  BIGINT not null,
    constraint SUBSCRIBERS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS,
    constraint SUBSCRIBERS_USERS_USER_ID_FK_2
        foreign key (SUB_ID) references USERS
);