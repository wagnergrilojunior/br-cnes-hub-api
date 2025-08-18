CREATE TABLE IF NOT EXISTS staging_estabelecimentos (
    cnes VARCHAR(7),
    nome TEXT,
    uf CHAR(2),
    municipio TEXT,
    cod_municipio VARCHAR(7),
    tipo_estab VARCHAR(4),
    logradouro TEXT,
    numero TEXT,
    bairro TEXT,
    cep VARCHAR(8),
    telefone TEXT,
    email TEXT,
    competencia VARCHAR(6),
    raw_json JSONB,
    created_at TIMESTAMP DEFAULT now()
);

CREATE TABLE IF NOT EXISTS core_estabelecimento (
    id BIGSERIAL PRIMARY KEY,
    cnes VARCHAR(7) UNIQUE,
    nome TEXT,
    uf CHAR(2),
    municipio TEXT,
    cod_municipio VARCHAR(7),
    tipo_estab VARCHAR(4),
    endereco JSONB,
    contato JSONB,
    competencia VARCHAR(6),
    updated_at TIMESTAMP
);

CREATE UNIQUE INDEX IF NOT EXISTS idx_core_estabelecimento_cnes ON core_estabelecimento (cnes);
CREATE INDEX IF NOT EXISTS idx_core_estabelecimento_uf ON core_estabelecimento (uf);
CREATE INDEX IF NOT EXISTS idx_core_estabelecimento_municipio ON core_estabelecimento (municipio);
CREATE INDEX IF NOT EXISTS idx_core_estabelecimento_tipo_estab ON core_estabelecimento (tipo_estab);
