CREATE TABLE assembleia
(
    id     bigint    not null auto_increment,
    inicio timestamp NOT NULL,
    fim    timestamp,
    status text      NOT NULL,
    primary key (id)
);

CREATE TABLE pauta
(
    id            bigint       not null auto_increment,
    titulo        varchar(100) NOT NULL,
    status        text         NOT NULL,
    primary key (id)
);

CREATE TABLE sessao_de_votacao
(
    id       bigint    not null auto_increment,
    inicio   timestamp NOT NULL,
    fim      timestamp,
    status   text      NOT NULL,
    pauta_id bigint    NOT NULL,
    primary key (id),
    FOREIGN KEY (pauta_id) REFERENCES pauta (id)
);

CREATE TABLE voto
(
    id                   bigint not null auto_increment,
    status               text   NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE associado
(
    id      bigint       not null auto_increment,
    nome    varchar(100) NOT NULL,
    cpf     varchar(11)  NOT NULL,
    status  text         NOT NULL,
    primary key (id)
);

CREATE TABLE assembleia_pautas
(
    assembleia_id bigint NOT NULL,
    pauta_id      bigint NOT NULL,
    FOREIGN KEY (assembleia_id) REFERENCES assembleia (id),
    FOREIGN KEY (pauta_id) REFERENCES pauta (id)
);

CREATE TABLE sessao_de_votacao_votos
(
    sessao_de_votacao_id bigint NOT NULL,
    voto_id              bigint NOT NULL,
    FOREIGN KEY (sessao_de_votacao_id) REFERENCES sessao_de_votacao (id),
    FOREIGN KEY (voto_id) REFERENCES voto (id)
);

CREATE TABLE voto_associado
(
    voto_id      bigint NOT NULL,
    associado_id bigint NOT NULL,
    FOREIGN KEY (voto_id) REFERENCES voto (id),
    FOREIGN KEY (associado_id) REFERENCES associado (id)
);