# noinspection SqlInsertValuesForFile

INSERT INTO associado (nome, cpf, status)
VALUES ('João', '12345678901', 'PODE_VOTAR');

INSERT INTO associado (nome, cpf, status)
VALUES ('Maria', '12345678902', 'PODE_VOTAR');

INSERT INTO assembleia (inicio, status)
VALUES ('2023-02-27 08:00:00', 'INICIADA');

INSERT INTO pauta (titulo, status)
VALUES ('pauta 1', 'AGUARDANDO_VOTACAO');

INSERT INTO pauta (titulo, status)
VALUES ('pauta 2', 'AGUARDANDO_VOTACAO');

INSERT INTO sessao_de_votacao (inicio, status, pauta_id)
VALUES ('2023-02-27 08:30:00', 'INICIADA', 1);

INSERT INTO sessao_de_votacao (inicio, status, pauta_id)
VALUES ('2023-02-27 09:00:00', 'INICIADA', 2);

INSERT INTO voto (status)
VALUES ('SIM');

INSERT INTO voto (status)
VALUES ('SIM');

INSERT INTO voto (status)
VALUES ('NAO');

INSERT INTO voto (status)
VALUES ('NAO');

INSERT INTO assembleia_pautas (assembleia_id, pauta_id)
VALUES (1, 1);

INSERT INTO assembleia_pautas (assembleia_id, pauta_id)
VALUES (1, 2);

INSERT INTO sessao_de_votacao_votos (sessao_de_votacao_id, voto_id)
VALUES (1, 1);

INSERT INTO sessao_de_votacao_votos (sessao_de_votacao_id, voto_id)
VALUES (1, 2);

INSERT INTO sessao_de_votacao_votos (sessao_de_votacao_id, voto_id)
VALUES (2, 3);

INSERT INTO sessao_de_votacao_votos (sessao_de_votacao_id, voto_id)
VALUES (2, 4);

INSERT INTO voto_associado (voto_id, associado_id)
VALUES (1, 1);

INSERT INTO voto_associado (voto_id, associado_id)
VALUES (2, 2);

INSERT INTO voto_associado (voto_id, associado_id)
VALUES (3, 1);

INSERT INTO voto_associado (voto_id, associado_id)
VALUES (4, 2);



