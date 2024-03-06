
CREATE TABLE PARCELA (
    ID_PARCELA SERIAL PRIMARY KEY,
    QTD_PAGA INT,
    QTD_RESTANTE INT
);

CREATE TABLE PAGAMENTO (
    ID_PAGAMENTO SERIAL PRIMARY KEY,
    NOME VARCHAR(255),
    CRIADO_EM TIMESTAMP
 );

CREATE TABLE ITEM (
    ID_ITEM SERIAL PRIMARY KEY,
    NOME VARCHAR(255),
    ID_PAGAMENTO BIGINT REFERENCES PAGAMENTO(ID_PAGAMENTO),
    ID_PARCELA BIGINT REFERENCES PARCELA(id_parcela),
    VALOR DOUBLE PRECISION,
    VALOR_RESTANTE DOUBLE PRECISION,
    VALOR_TOTAL DOUBLE PRECISION,
    STATUS VARCHAR(255),
    PAGO_NO_MES BOOLEAN,
    CRIADO_EM TIMESTAMP WITH TIME ZONE
);


-- Tabelas history

CREATE TABLE PARCELA_HISTORY (
    ID_PARCELA BIGINT PRIMARY KEY,
    QTD_PAGA INT,
    QTD_RESTANTE INT
);

CREATE TABLE ITEM_HISTORY (
    ID_ITEM BIGINT PRIMARY KEY,
    NOME VARCHAR(255),
    BANCO VARCHAR(255),
    parcela_id BIGINT REFERENCES PARCELA_HISTORY(ID_PARCELA),
    VALOR DOUBLE PRECISION,
    VALOR_RESTANTE DOUBLE PRECISION,
    VALOR_TOTAL DOUBLE PRECISION,
    STATUS VARCHAR(255),
    CRIADO_EM TIMESTAMP WITH TIME ZONE
);
