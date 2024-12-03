CREATE TABLE PRODUTOS (
    ID NUMBER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    NOME VARCHAR2(100) NOT NULL,
    PRECO NUMBER(10,2) NOT NULL,
    FABRICANTE_ID NUMBER,
    PRIMARY KEY (ID),
    CONSTRAINT FK_PRODUTOS_FABRICANTES FOREIGN KEY (FABRICANTE_ID)
        REFERENCES FABRICANTES(ID) ON DELETE SET NULL
);