-- 1) Preparação: remover DB (opcional) e criar novo banco (executar como superuser)
DROP DATABASE IF EXISTS turismo;
CREATE DATABASE turismo WITH ENCODING 'UTF8' OWNER postgres;

-- 2) Tabelas relacionais principais

-- Usuário
DROP TABLE IF EXISTS usuario CASCADE;
CREATE TABLE usuario (
  id BIGSERIAL PRIMARY KEY,
  login VARCHAR(80) NOT NULL UNIQUE,
  email VARCHAR(255) NOT NULL UNIQUE,
  senha_hash VARCHAR(255) NOT NULL,
  role VARCHAR(20) NOT NULL DEFAULT 'USER',
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

-- Ponto Turístico
DROP TABLE IF EXISTS ponto_turistico CASCADE;
CREATE TABLE ponto_turistico (
  id BIGSERIAL PRIMARY KEY,
  nome VARCHAR(255) NOT NULL,
  descricao TEXT NOT NULL,
  cidade VARCHAR(120) NOT NULL,
  estado VARCHAR(80),
  pais VARCHAR(80),
  latitude NUMERIC(10,7),
  longitude NUMERIC(10,7),
  endereco TEXT,
  criado_por BIGINT REFERENCES usuario(id) ON DELETE SET NULL,
  nota_media NUMERIC(3,2) NOT NULL DEFAULT 0.00,
  qtd_avaliacoes INTEGER NOT NULL DEFAULT 0,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT uq_nome_cidade UNIQUE (nome, cidade),
  CONSTRAINT chk_nome_nonempty CHECK (char_length(trim(nome)) > 0),
  CONSTRAINT chk_cidade_nonempty CHECK (char_length(trim(cidade)) > 0)
);

-- Hospedagem
DROP TABLE IF EXISTS hospedagem CASCADE;
CREATE TABLE hospedagem (
  id BIGSERIAL PRIMARY KEY,
  ponto_id BIGINT NOT NULL REFERENCES ponto_turistico(id) ON DELETE CASCADE,
  nome VARCHAR(255) NOT NULL,
  endereco TEXT,
  telefone VARCHAR(50),
  preco_medio NUMERIC(10,2),
  tipo VARCHAR(30),
  link_reserva TEXT
);

-- Avaliação
DROP TABLE IF EXISTS avaliacao CASCADE;
CREATE TABLE avaliacao (
  id BIGSERIAL PRIMARY KEY,
  ponto_id BIGINT NOT NULL REFERENCES ponto_turistico(id) ON DELETE CASCADE,
  usuario_id BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
  nota INTEGER NOT NULL CHECK (nota BETWEEN 1 AND 5),
  comentario TEXT,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  CONSTRAINT uq_ponto_usuario UNIQUE (ponto_id, usuario_id)
);

-- Favoritos (marcação do usuário)
DROP TABLE IF EXISTS usuario_favorito CASCADE;
CREATE TABLE usuario_favorito (
  usuario_id BIGINT NOT NULL REFERENCES usuario(id) ON DELETE CASCADE,
  ponto_id BIGINT NOT NULL REFERENCES ponto_turistico(id) ON DELETE CASCADE,
  favorited_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
  PRIMARY KEY (usuario_id, ponto_id)
);

-- Índices úteis
CREATE INDEX idx_ponto_cidade ON ponto_turistico(cidade);
CREATE INDEX idx_ponto_nota_media ON ponto_turistico(nota_media);
CREATE INDEX idx_avaliacao_ponto_created ON avaliacao(ponto_id, created_at DESC);

-- 2) Função e triggers: recalcula nota média e contador de avaliações
-- A função atualiza nota_media e qtd_avaliacoes em ponto_turistico

DROP FUNCTION IF EXISTS recalc_nota_media(bigint);
CREATE OR REPLACE FUNCTION recalc_nota_media(p_ponto_id BIGINT) RETURNS VOID LANGUAGE plpgsql AS $$
DECLARE
  v_avg NUMERIC(3,2);
  v_count INTEGER;
BEGIN
  SELECT COUNT(*) , COALESCE(AVG(nota),0) INTO v_count, v_avg
    FROM avaliacao
    WHERE ponto_id = p_ponto_id;

  IF v_count = 0 THEN
    v_avg := 0;
  END IF;

  UPDATE ponto_turistico
    SET nota_media = ROUND(v_avg::numeric, 2),
        qtd_avaliacoes = v_count
    WHERE id = p_ponto_id;
END;
$$;

-- Trigger wrapper for AFTER INSERT/UPDATE/DELETE on avaliacao
DROP FUNCTION IF EXISTS trg_avaliacao_after();
CREATE OR REPLACE FUNCTION trg_avaliacao_after() RETURNS TRIGGER LANGUAGE plpgsql AS $$
BEGIN
  IF (TG_OP = 'INSERT') THEN
    PERFORM recalc_nota_media(NEW.ponto_id);
    RETURN NEW;
  ELSIF (TG_OP = 'UPDATE') THEN
    -- se ponto mudou, recalc for old and new
    IF NEW.ponto_id IS DISTINCT FROM OLD.ponto_id THEN
      PERFORM recalc_nota_media(OLD.ponto_id);
      PERFORM recalc_nota_media(NEW.ponto_id);
    ELSE
      PERFORM recalc_nota_media(NEW.ponto_id);
    END IF;
    RETURN NEW;
  ELSIF (TG_OP = 'DELETE') THEN
    PERFORM recalc_nota_media(OLD.ponto_id);
    RETURN OLD;
  END IF;
  RETURN NULL;
END;
$$;

DROP TRIGGER IF EXISTS trg_avaliacao_after ON avaliacao;
CREATE TRIGGER trg_avaliacao_after
  AFTER INSERT OR UPDATE OR DELETE ON avaliacao
  FOR EACH ROW EXECUTE FUNCTION trg_avaliacao_after();
