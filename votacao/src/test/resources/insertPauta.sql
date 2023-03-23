INSERT INTO assembleia (inicio, status)
VALUES ('2023-02-27T08:00:00', 'INICIADA');

INSERT INTO pauta (titulo, status)
VALUES ('pauta 1', 'AGUARDANDO_VOTACAO'),
       ('pauta 2', 'APROVADA');

INSERT INTO assembleia_pautas (assembleia_id, pauta_id)
VALUES (1, 1),
       (1, 2);