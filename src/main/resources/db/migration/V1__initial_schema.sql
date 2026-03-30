-- =============================================================================
-- Migration Inicial – Sistema de Pedidos
-- Banco: PostgreSQL 15+ (Supabase)
-- =============================================================================

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE produto_servico (
    id               UUID           NOT NULL DEFAULT gen_random_uuid(),
    nome             VARCHAR(150)   NOT NULL,
    preco            NUMERIC(15, 2) NOT NULL CHECK (preco > 0),
    ativo            BOOLEAN        NOT NULL DEFAULT TRUE,
    tipo_item        VARCHAR(20)    NOT NULL CHECK (tipo_item IN ('PRODUTO', 'SERVICO')),
    version          INTEGER        NOT NULL DEFAULT 0,

    CONSTRAINT pk_produto_servico PRIMARY KEY (id)
);

CREATE TABLE pedido (
    id                   UUID           NOT NULL DEFAULT gen_random_uuid(),
    status_pedido        VARCHAR(20)    NOT NULL DEFAULT 'ABERTO'
                             CHECK (status_pedido IN ('ABERTO', 'FECHADO')),
    percentual_desconto  NUMERIC(5, 2)  NOT NULL DEFAULT 0.00
                             CHECK (percentual_desconto >= 0 AND percentual_desconto <= 100),
    valor_total_desconto NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    valor_total_pedido   NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
    version              INTEGER        NOT NULL DEFAULT 0,

    CONSTRAINT pk_pedido PRIMARY KEY (id)
);

CREATE TABLE item_pedido (
    id                 UUID           NOT NULL DEFAULT gen_random_uuid(),
    pedido_id          UUID           NOT NULL,
    produto_servico_id UUID           NOT NULL,
    quantidade         INTEGER        NOT NULL CHECK (quantidade >= 1),
    preco_unitario     NUMERIC(15, 2) NOT NULL CHECK (preco_unitario > 0),
    version            INTEGER        NOT NULL DEFAULT 0,

    CONSTRAINT pk_item_pedido PRIMARY KEY (id),

    CONSTRAINT fk_item_pedido_pedido
        FOREIGN KEY (pedido_id)
        REFERENCES pedido (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_item_pedido_produto_servico
        FOREIGN KEY (produto_servico_id)
        REFERENCES produto_servico (id)
        ON DELETE RESTRICT
);

CREATE INDEX idx_item_pedido_pedido_id         ON item_pedido (pedido_id);
CREATE INDEX idx_item_pedido_produto_servico_id ON item_pedido (produto_servico_id);
