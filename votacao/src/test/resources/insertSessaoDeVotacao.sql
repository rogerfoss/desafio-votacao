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

INSERT INTO assembleia_pautas (assembleia_id, pauta_id)
VALUES (1, 1);