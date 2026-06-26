USE bd_timeright;
GO

DROP TABLE IF EXISTS Agendamento;
DROP TABLE IF EXISTS Funcionario;
DROP TABLE IF EXISTS Usuario;
DROP TABLE IF EXISTS Servico;
DROP TABLE IF EXISTS NivelAcesso;
DROP TABLE IF EXISTS Salao;
GO

-- =========================
-- NÍVEL DE ACESSO
-- =========================
CREATE TABLE NivelAcesso (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL
);

-- DADOS PADRÃO
INSERT INTO NivelAcesso (nome, status)
VALUES
('ADM', 'ATIVO'),
('MANAGER', 'ATIVO'),
('CLIENTE', 'ATIVO');

-- =========================
-- USUÁRIO
-- =========================
CREATE TABLE Usuario (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
    password VARCHAR(255) NOT NULL,
    data_cadastro SMALLDATETIME NOT NULL,
    data_atualizacao SMALLDATETIME NULL,
    nivel_acesso_id INT NOT NULL,
    status VARCHAR(20) NOT NULL,

    CONSTRAINT FK_Usuario_NivelAcesso
        FOREIGN KEY (nivel_acesso_id)
        REFERENCES NivelAcesso(id)
);

-- =========================
-- SALÃO
-- =========================
CREATE TABLE Salao (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cnpj VARCHAR(18) NOT NULL,
    email VARCHAR(100) NOT NULL,
    endereco VARCHAR(200) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL
);

-- =========================
-- SERVIÇO
-- =========================
CREATE TABLE Servico (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    descricao VARCHAR(300),
    preco DECIMAL(10,2) NOT NULL,
    duracao INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    salao_id INT NOT NULL,

    CONSTRAINT FK_Servico_Salao
        FOREIGN KEY (salao_id)
        REFERENCES Salao(id)
);

-- =========================
-- FUNCIONÁRIO
-- =========================
CREATE TABLE Funcionario (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    observacoes VARCHAR(225),
    status VARCHAR(20) NOT NULL,
    salao_id INT NOT NULL,

    CONSTRAINT FK_Funcionario_Salao
        FOREIGN KEY (salao_id)
        REFERENCES Salao(id)
);

-- =========================
-- AGENDAMENTO
-- =========================
CREATE TABLE Agendamento (
    id INT IDENTITY(1,1) PRIMARY KEY,
    usuario_id INT NOT NULL,
    funcionario_id INT NOT NULL,
    servico_id INT NOT NULL,
    data_hora SMALLDATETIME NOT NULL,
    status VARCHAR(20) NOT NULL,

    CONSTRAINT FK_Agendamento_Usuario
        FOREIGN KEY (usuario_id)
        REFERENCES Usuario(id),

    CONSTRAINT FK_Agendamento_Funcionario
        FOREIGN KEY (funcionario_id)
        REFERENCES Funcionario(id),

    CONSTRAINT FK_Agendamento_Servico
        FOREIGN KEY (servico_id)
        REFERENCES Servico(id)
);
GO

-- =========================
-- TRIGGER
-- =========================
CREATE TRIGGER trg_agendamento_cliente
ON Agendamento
INSTEAD OF INSERT
AS
BEGIN
    IF EXISTS (
        SELECT 1
        FROM inserted i
        JOIN Usuario u ON i.usuario_id = u.id
        JOIN NivelAcesso n ON u.nivel_acesso_id = n.id
        WHERE UPPER(n.nome) <> 'CLIENTE'
    )
    BEGIN
        RAISERROR('Apenas clientes podem realizar agendamentos.',16,1);
        RETURN;
    END

    INSERT INTO Agendamento
    (
        usuario_id,
        funcionario_id,
        servico_id,
        data_hora,
        status
    )
    SELECT
        usuario_id,
        funcionario_id,
        servico_id,
        data_hora,
        status
    FROM inserted;
END;
GO

-- CONSULTAS
SELECT * FROM NivelAcesso;
SELECT * FROM Usuario;
SELECT * FROM Salao;
SELECT * FROM Servico;
SELECT * FROM Funcionario;
SELECT * FROM Agendamento;