INSERT INTO assembleia (inicio, status)
VALUES ('2023-02-27T08:00:00', 'INICIADA');

INSERT INTO pauta (titulo, status, assembleia_id)
VALUES ('pauta 1', 'AGUARDANDO_VOTACAO', 1);

INSERT INTO pauta (titulo, status, assembleia_id)
VALUES ('pauta 2', 'APROVADA', 1);

INSERT INTO assembleia_pautas (assembleia_id, pauta_id)
VALUES (1, 1);

INSERT INTO assembleia_pautas (assembleia_id, pauta_id)
VALUES (1, 2);