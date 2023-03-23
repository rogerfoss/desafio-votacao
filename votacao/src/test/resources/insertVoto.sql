INSERT INTO associado (nome, cpf, status)
VALUES ('João Ferreira', '52342310030', 'PODE_VOTAR'),
       ('Maria Santos', '02996891074', 'PODE_VOTAR');

INSERT INTO assembleia (inicio, status)
VALUES ('2023-02-27T08:00:00', 'INICIADA');

INSERT INTO pauta (titulo, status)
VALUES ('pauta 1', 'AGUARDANDO_VOTACAO'),
       ('pauta 2', 'AGUARDANDO_VOTACAO');

INSERT INTO sessao_de_votacao (inicio, status, pauta_id)
VALUES ('2023-02-27 08:30:00', 'INICIADA', 1),
       ('2023-02-27 09:00:00', 'ENCERRADA', 2);

INSERT INTO voto (status, sessao_de_votacao_id)
VALUES ('SIM', 1),
       ('SIM', 1),
       ('NAO', 2),
       ('NAO', 2);

INSERT INTO assembleia_pautas (assembleia_id, pauta_id)
VALUES (1, 1);

INSERT INTO pauta_sessao_de_votacao (pauta_id, sessao_de_votacao_id)
VALUES (1, 1),
       (2, 2);

INSERT INTO sessao_de_votacao_votos (sessao_de_votacao_id, voto_id)
VALUES (1, 1),
       (1, 2),
       (2, 3),
       (2, 4);

INSERT INTO voto_associado (voto_id, associado_id)
VALUES (1, 1),
       (2, 2),
       (3, 1),
       (4, 2);