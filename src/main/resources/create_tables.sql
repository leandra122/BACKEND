-- =============================================
-- TimeRight - Script de criação das tabelas
-- SQL Server
-- =============================================

CREATE TABLE Salao (
    id         BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome       NVARCHAR(100)  NOT NULL,
    cnpj       NVARCHAR(18)   NOT NULL UNIQUE,
    telefone   NVARCHAR(20),
    email      NVARCHAR(100),
    endereco   NVARCHAR(255),
    status     NVARCHAR(10)   NOT NULL DEFAULT 'ATIVO'
);

CREATE TABLE Funcionario (
    id           BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome         NVARCHAR(100) NOT NULL,
    telefone     NVARCHAR(20),
    email        NVARCHAR(100),
    especialidade NVARCHAR(100),
    status       NVARCHAR(10)  NOT NULL DEFAULT 'ATIVO',
    salao_id     BIGINT        NOT NULL,
    CONSTRAINT FK_Funcionario_Salao FOREIGN KEY (salao_id) REFERENCES Salao(id)
);

CREATE TABLE Servico (
    id        BIGINT IDENTITY(1,1) PRIMARY KEY,
    nome      NVARCHAR(100)  NOT NULL,
    descricao NVARCHAR(255),
    preco     DECIMAL(10, 2) NOT NULL,
    duracao   INT            NOT NULL,
    status    NVARCHAR(10)   NOT NULL DEFAULT 'ATIVO',
    salao_id  BIGINT         NOT NULL,
    CONSTRAINT FK_Servico_Salao FOREIGN KEY (salao_id) REFERENCES Salao(id)
);

CREATE TABLE Agendamento (
    id             BIGINT IDENTITY(1,1) PRIMARY KEY,
    data_hora      DATETIME2      NOT NULL,
    status         NVARCHAR(20)   NOT NULL DEFAULT 'AGENDADO',
    observacoes    NVARCHAR(255),
    usuario_id     BIGINT         NOT NULL,
    funcionario_id BIGINT         NOT NULL,
    servico_id     BIGINT         NOT NULL,
    CONSTRAINT FK_Agendamento_Usuario     FOREIGN KEY (usuario_id)     REFERENCES Usuario(id),
    CONSTRAINT FK_Agendamento_Funcionario FOREIGN KEY (funcionario_id) REFERENCES Funcionario(id),
    CONSTRAINT FK_Agendamento_Servico     FOREIGN KEY (servico_id)     REFERENCES Servico(id)
);
