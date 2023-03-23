DELETE
FROM voto_associado;
DELETE
FROM sessao_de_votacao_votos;
DELETE
FROM assembleia_pautas;
DELETE
FROM voto;
ALTER TABLE voto
    ALTER COLUMN id RESTART WITH 1;
DELETE
FROM sessao_de_votacao;
ALTER TABLE sessao_de_votacao
    ALTER COLUMN id RESTART WITH 1;
DELETE
FROM pauta;
ALTER TABLE pauta
    ALTER COLUMN id RESTART WITH 1;
DELETE
FROM assembleia;
ALTER TABLE assembleia
    ALTER COLUMN id RESTART WITH 1;
DELETE
FROM associado;
ALTER TABLE associado
    ALTER COLUMN id RESTART WITH 1;